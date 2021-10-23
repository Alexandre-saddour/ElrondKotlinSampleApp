package fr.asaddour.elrondkotlinsdk.domain.showcase

import android.util.Log
import com.elrond.erdkotlin.domain.account.models.Account
import com.elrond.erdkotlin.domain.esdt.management.SetSpecialRolesEsdtUsecase
import com.elrond.erdkotlin.domain.esdt.models.EsdtSpecialRole
import com.elrond.erdkotlin.domain.networkconfig.models.NetworkConfig
import com.elrond.erdkotlin.domain.nft.CreateNftUsecase
import com.elrond.erdkotlin.domain.nft.IssueNftUsecase
import com.elrond.erdkotlin.domain.nft.TransferNFTCreateRoleUsecase
import com.elrond.erdkotlin.domain.wallet.models.Address
import com.elrond.erdkotlin.domain.wallet.models.Wallet
import kotlinx.coroutines.delay
import java.lang.IllegalArgumentException
import javax.inject.Inject
import kotlin.random.Random

class ShowcaseNftIssuanceUsecase @Inject constructor(
    private val issueNftUsecase: IssueNftUsecase,
    private val createNftUsecase: CreateNftUsecase,
    private val transferNFTCreateRoleUsecase: TransferNFTCreateRoleUsecase,
    private val setSpecialRolesEsdtUsecase: SetSpecialRolesEsdtUsecase,
    private val pollNftTokenIdentifierUsecase: PollNftTokenIdentifierUsecase,
    private val pollTransactionUsecase: PollTransactionUsecase
) {

    // 1- Issue NFT
    // 2- set ESDTRoleNFTCreate
    // 3- create NFT
    suspend fun execute(
        account: Account,
        wallet: Wallet,
        networkConfig: NetworkConfig
    ) {
        // 1- Issue NFT
        val random = Random.nextInt(0, 10000)
        val issueTransaction = issueNftUsecase.execute(
            account = account,
            wallet = wallet,
            networkConfig = networkConfig,
            gasPrice = networkConfig.minGasPrice,
            tokenName = "ErdKotlinNft${random}",
            tokenTicker = "EKT${random}",
            type = IssueNftUsecase.Type.NFT,
            canTransferNFTCreateRole = true
        )

        // wait for transaction and extract the identifier
        val tokenIdentifier = pollNftTokenIdentifierUsecase.execute(
            txHash = issueTransaction.txHash,
            withResults = true
        ) ?: throw IllegalArgumentException("NFT tokenIdentifier not found")

        // 2- set ESDTRoleNFTCreate
        val setSpecialRolesTransaction = setSpecialRolesEsdtUsecase.execute(
            account = account.copy(nonce = account.nonce + 1),
            wallet = wallet,
            networkConfig = networkConfig,
            gasPrice = networkConfig.minGasPrice,
            tokenIdentifier = tokenIdentifier,
            addressToUpdate = account.address,
            specialRoles = listOf(EsdtSpecialRole.ESDTRoleNFTCreate),
            action = SetSpecialRolesEsdtUsecase.Action.Set
        )

        // wait for ESDTRoleNFTCreate to be set
        pollTransactionUsecase.execute(
            txHash = setSpecialRolesTransaction.txHash,
            withResults = true,
            predicate = { infos -> infos.smartContractResults != null }
        )

        Log.d(TAG, "10sec delay 1/3")
        delay(10_000)
        Log.d(TAG, "10sec delay 2/3")
        delay(10_000)
        Log.d(TAG, "10sec delay 3/3")
        delay(10_000)
        Log.d(TAG, "Delay finished")

        // 3- create NFT
        val creationTransaction = createNftUsecase.execute(
            account = account.copy(nonce = account.nonce + 2),
            wallet = wallet,
            networkConfig = networkConfig,
            gasPrice = networkConfig.minGasPrice,
            tokenIdentifier = tokenIdentifier,
            initialQuantity = 1.toBigInteger(),
            nftName = "ErdKotlinNft",
            royalties = 0,
            uris = listOf("https://github.com/ElrondNetwork/elrond-sdk-erdkotlin"),
            hash = "kotlin sdk for Elrond",
            attributes = "ErdKotlin attributes"
        )
        Log.d(TAG, "creationTransaction: $creationTransaction")

        // wait for nft transaction creation to finish
        pollTransactionUsecase.execute(
            txHash = creationTransaction.txHash,
            withResults = true,
            predicate = { infos -> infos.smartContractResults != null }
        )

        transferNFTCreateRoleUsecase.execute(
            account = account.copy(nonce = account.nonce + 3),
            wallet = wallet,
            networkConfig = networkConfig,
            gasPrice = networkConfig.minGasPrice,
            tokenIdentifier = tokenIdentifier,
            roleReceiver = Address.fromBech32("erd1sytay9nan2fnjx069tesszrh5lxns940w3pgay8qltqrwcsquq9s44ee83"),
        )


    }

    companion object {
        const val TAG = "ShowcaseNftIssuance"
    }

}