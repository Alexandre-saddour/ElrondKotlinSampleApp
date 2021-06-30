package fr.asaddour.elrondkotlinsdk.domain.showcase

import com.elrond.erdkotlin.domain.account.models.Account
import com.elrond.erdkotlin.domain.esdt.*
import com.elrond.erdkotlin.domain.networkconfig.models.NetworkConfig
import com.elrond.erdkotlin.domain.wallet.models.Wallet
import javax.inject.Inject
import kotlin.random.Random

class ShowcaseEsdtIssuanceUsecase @Inject constructor(
    private val issuedEsdtUsecase: IssueEsdtUsecase
) {

    fun execute(
        account: Account,
        wallet: Wallet,
        networkConfig: NetworkConfig,
    ) {
        val random = Random.nextInt(0,10000)
        issuedEsdtUsecase.execute(
            account = account,
            wallet = wallet,
            networkConfig = networkConfig,
            tokenName = "ErdKotlin${random}",
            tokenTicker = "EKT${random}",
            initialSupply = 100000.toBigInteger(),
            numberOfDecimal = 3,
            canFreeze = true,
            canWipe = false,
            canPause = true,
            canMint = true,
            canBurn = true,
            canChangeOwner = true,
            canUpgrade = true,
            canAddSpecialRoles = true,
        )
    }
    companion object {
        const val TAG = "ShowcaseEsdtIssuanceUsecase"
    }

}