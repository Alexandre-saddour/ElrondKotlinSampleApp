package fr.asaddour.elrondkotlinsdk.domain.showcase

import android.util.Log
import com.elrond.erdkotlin.domain.account.models.Account
import com.elrond.erdkotlin.domain.esdt.management.SetSpecialRolesEsdtUsecase
import com.elrond.erdkotlin.domain.esdt.models.EsdtSpecialRole
import com.elrond.erdkotlin.domain.networkconfig.models.NetworkConfig
import com.elrond.erdkotlin.domain.nft.CreateNftUsecase
import com.elrond.erdkotlin.domain.nft.IssueNftUsecase
import com.elrond.erdkotlin.domain.wallet.models.Wallet
import java.lang.IllegalArgumentException
import javax.inject.Inject
import kotlin.random.Random

class ShowcaseNftIssuanceUsecase @Inject constructor(
    private val issueNftUsecase: IssueNftUsecase,
    private val createNftUsecase: CreateNftUsecase,
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
            type = IssueNftUsecase.Type.NFT
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
    }

    companion object {
        const val TAG = "ShowcaseNftIssuance"
    }

}