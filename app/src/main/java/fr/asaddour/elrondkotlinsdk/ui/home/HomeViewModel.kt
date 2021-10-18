package fr.asaddour.elrondkotlinsdk.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elrond.erdkotlin.Exceptions
import com.elrond.erdkotlin.domain.wallet.models.Address
import com.elrond.erdkotlin.domain.wallet.models.Wallet
import com.elrond.erdkotlin.domain.account.GetAccountUsecase
import com.elrond.erdkotlin.domain.networkconfig.GetNetworkConfigUsecase
import com.elrond.erdkotlin.domain.transaction.*
import com.elrond.erdkotlin.domain.transaction.models.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.asaddour.elrondkotlinsdk.domain.showcase.ShowcaseEsdtApiUsecase
import fr.asaddour.elrondkotlinsdk.domain.showcase.ShowcaseEsdtIssuanceUsecase
import fr.asaddour.elrondkotlinsdk.domain.showcase.ShowcaseNftIssuanceUsecase
import fr.asaddour.elrondkotlinsdk.domain.transaction.PollTransactionInfoUsecase
import fr.asaddour.elrondkotlinsdk.domain.transaction.PollTransactionStatusUsecase
import fr.asaddour.elrondkotlinsdk.domain.wallet.DeleteCurrentWalletUsecase
import fr.asaddour.elrondkotlinsdk.domain.wallet.LoadCurrentWalletUsecase
import fr.asaddour.elrondkotlinsdk.utils.SingleLiveEvent
import fr.asaddour.elrondkotlinsdk.utils.ext.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val loadCurrentWalletUsecase: LoadCurrentWalletUsecase,
    private val deleteCurrentWalletUsecase: DeleteCurrentWalletUsecase,
    private val getAccountUsecase: GetAccountUsecase,
    private val sendTransactionUsecase: SendTransactionUsecase,
    private val estimateCostOfTransactionUsecase: EstimateCostOfTransactionUsecase,
    private val pollTransactionStatusUsecase: PollTransactionStatusUsecase,
    private val pollTransactionInfoUsecase: PollTransactionInfoUsecase,
    private val getAddressTransactionsUsecase: GetAddressTransactionsUsecase,
    private val getNetworkConfigUsecase: GetNetworkConfigUsecase,
    private val showcaseNftIssuanceUsecase: ShowcaseNftIssuanceUsecase,
    private val showcaseEsdtIssuanceUsecase: ShowcaseEsdtIssuanceUsecase,
    private val showcaseEsdtUsecase: ShowcaseEsdtApiUsecase
) : ViewModel() {


    private val _viewAction = SingleLiveEvent<HomeAction>()
    val viewAction: LiveData<HomeAction> = _viewAction

    private val _viewState = MutableLiveData<HomeViewState>(HomeViewState.Loading)
    val viewState: LiveData<HomeViewState> = _viewState

    private var wallet: Wallet? = null

    fun refreshAccountData() = launch(Dispatchers.IO) {
        _viewState.postValue(HomeViewState.Loading)
        // load wallet only once
        val wallet = wallet ?: loadCurrentWalletUsecase.execute()?.also { wallet = it }

        wallet ?: run {
            // we don't have any wallet
            // lets create one
            _viewAction.postValue(HomeAction.OpenCreateWalletScreen)
            return@launch
        }

        // load account
        val address = Address.fromHex(wallet.publicKeyHex)
        val account = getAccountUsecase.execute(address)
        val accountUi = account.toUi()
        val state = when (val state = _viewState.value) {
            is HomeViewState.Content -> state.copy(
                account = accountUi
            )
            else -> HomeViewState.Content(
                account = accountUi,
                sentTransaction = null
            )
        }

        _viewState.postValue(state)

        if (account.balance != BigInteger.ZERO) {
            showcaseNftIssuanceUsecase.execute(
                account = account,
                wallet = wallet,
                networkConfig = getNetworkConfigUsecase.execute()
            )
//            showcaseEsdtIssuanceUsecase.execute(
//                account = account,
//                wallet = wallet,
//                networkConfig = getNetworkConfigUsecase.execute()
//            )
//            showcaseEsdtUsecase.execute(address)


        }
//        logTransactions(address)
    }

    // keeping this until a dedicated screen is done.
    private fun logTransactions(address: Address) {
        val transactions = getAddressTransactionsUsecase.execute(address)
        if (transactions.isNotEmpty()) {
            val transaction = transactions.first()
            Log.d("HomeViewModel", "transaction:$transaction")
        } else {
            Log.d("HomeViewModel", "no transaction for address:${address}")
        }
    }

    fun sendTransaction(
        toAddress: String,
        amount: String?, // 1 xEGLD is 1000000000000000000
        message: String?
    ) {
        val receiverAddress = extractAddress(toAddress) ?: run {
            // receiver address is invalid
            _viewAction.value = HomeAction.InvalidReceiverAddress
            return
        }

        val wallet = requireNotNull(wallet)
        val value = when (amount) {
            null -> BigInteger.ZERO
            else -> amount.toBigInteger()
        }
        launch(Dispatchers.IO) {
            // loading account from remote to make sure we have the right once
            val account = getAccountUsecase.execute(Address.fromHex(wallet.publicKeyHex))
            val networkConfig = getNetworkConfigUsecase.execute()
            val transaction = run {
                val transaction = Transaction(
                    sender = Address.fromHex(wallet.publicKeyHex),
                    receiver = receiverAddress,
                    value = value,
                    data = message,
                    chainID = networkConfig.chainID,
                    gasPrice = networkConfig.minGasPrice,
                    nonce = account.nonce
                )
                val gasLimit = estimateCostOfTransactionUsecase.execute(transaction)
                transaction.copy(gasLimit = gasLimit.toLong())
            }
            val sentTransaction = sendTransactionUsecase.execute(transaction, wallet)
            fetchTransactionStatus(txHash = sentTransaction.txHash)
        }
    }

    private fun extractAddress(toAddress: String) = try {
        when {
            Address.isValidBech32(toAddress) -> Address.fromBech32(toAddress)
            else -> Address.fromHex(toAddress)
        }
    } catch (e: Exceptions.AddressException) {
        e.printStackTrace()
        null
    } catch (e: Exceptions.BadAddressHrpException) {
        e.printStackTrace()
        null
    }

    fun deleteWallet() {
        launch {
            // delete wallet
            withContext(Dispatchers.IO) {
                wallet = null
                deleteCurrentWalletUsecase.execute()
            }

            // go to wallet creation screen.
            _viewAction.postValue(HomeAction.OpenCreateWalletScreen)
        }
    }

    fun fetchTransactionStatus(txHash: String) = launch(Dispatchers.IO) {
        Log.d("HomeViewModel", "fetching transaction status and info for $txHash")
        val transactionStatus = pollTransactionStatusUsecase.execute(txHash)
        val transactionInfo = pollTransactionInfoUsecase.execute(txHash)
        Log.d("HomeViewModel", "transaction status: $transactionStatus")
        Log.d("HomeViewModel", "transaction info: $transactionInfo")
        val state = _viewState.value as? HomeViewState.Content
        if (state != null) {
            _viewState.postValue(
                state.copy(
                    sentTransaction = SentTransaction(
                        txHash = txHash,
                        status = transactionStatus
                    )
                )
            )
        }
    }

    sealed class HomeViewState {
        object Loading : HomeViewState()
        data class Content(
            val account: AccountUi,
            val sentTransaction: SentTransaction?
        ) : HomeViewState()

    }

    data class AccountUi(
        val address: String,
        val balance: String,
        val nonce: String
    )

    data class SentTransaction(
        val txHash: String,
        val status: String,
    )

    sealed class HomeAction {
        object OpenCreateWalletScreen : HomeAction()
        object InvalidReceiverAddress : HomeAction()
    }


}
