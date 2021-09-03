package fr.asaddour.elrondkotlinsdk.domain.showcase

import com.elrond.erdkotlin.domain.account.models.Account
import com.elrond.erdkotlin.domain.esdt.GetAllEsdtUsecase
import com.elrond.erdkotlin.domain.esdt.GetAllIssuedEsdtUsecase
import com.elrond.erdkotlin.domain.esdt.management.*
import com.elrond.erdkotlin.domain.esdt.models.EsdtSpecialRole
import com.elrond.erdkotlin.domain.esdt.models.ManagementProperty
import com.elrond.erdkotlin.domain.networkconfig.models.NetworkConfig
import com.elrond.erdkotlin.domain.wallet.models.Address
import com.elrond.erdkotlin.domain.wallet.models.Wallet
import javax.inject.Inject
import kotlin.random.Random

class ShowcaseEsdtIssuanceUsecase @Inject constructor(
    private val issuedEsdtUsecase: IssueEsdtUsecase,
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
            gasPrice = networkConfig.minGasPrice,
            tokenName = "ErdKotlin${random}",
            tokenTicker = "EKT${random}",
            initialSupply = 100000.toBigInteger(),
            numberOfDecimal = 3,
            managementProperties = mapOf(
                ManagementProperty.CanFreeze to true,
                ManagementProperty.CanWipe to false,
                ManagementProperty.CanPause to true,
                ManagementProperty.CanMint to true,
                ManagementProperty.CanBurn to true,
                ManagementProperty.CanChangeOwner to true,
                ManagementProperty.CanUpgrade to true,
                ManagementProperty.CanAddSpecialRoles to true
            )
        )

//        val issuedToken = getAllEsdtUsecase.execute(account.address).toList().lastOrNull() ?: return

//        transferEsdtUsecase.execute(
//            account = account,
//            wallet = wallet,
//            networkConfig = networkConfig,
//            gasPrice = networkConfig.minGasPrice,
//            receiver = Address.fromBech32("erd1zdlhltfh0jzw9y70ugmxm42neafuek3v24fk36terhwwn5j5ctvspcxh6u"),
//            tokenIdentifier = issuedToken.second.tokenIdentifier,
//            valueToTransfer = 170.toBigInteger()
//        )


//        mintEsdtUsecase.execute(
//            account = account,
//            wallet = wallet,
//            networkConfig = networkConfig,
//            gasPrice = networkConfig.minGasPrice,
//            tokenIdentifier = issuedToken.second.tokenIdentifier,
//            supplyToMint = "130".toBigInteger(),
//        )

//        burnEsdtUsecase.execute(
//            account = account,
//            wallet = wallet,
//            networkConfig = networkConfig,
//            gasPrice = networkConfig.minGasPrice,
//            tokenIdentifier = issuedToken.second.tokenIdentifier,
//            supplyToBurn = "100".toBigInteger(),
//        )

//        upgradeEsdtUsecase.execute(
//            account = account,
//            wallet = wallet,
//            networkConfig = networkConfig,
//            gasPrice = networkConfig.minGasPrice,
//            tokenIdentifier = issuedToken.second.tokenIdentifier,
//            managementProperties = mapOf(
//                ManagementProperty.CanMint to false,
//                ManagementProperty.CanBurn to false,
//            )
//        )

//        setSpecialRolesEsdtUsecase.execute(
//            account = account,
//            wallet = wallet,
//            networkConfig = networkConfig,
//            gasPrice = networkConfig.minGasPrice,
//            tokenIdentifier = issuedToken.second.tokenIdentifier,
//            addressToUpdate = Address.fromBech32("erd1zdlhltfh0jzw9y70ugmxm42neafuek3v24fk36terhwwn5j5ctvspcxh6u"),
//            specialRoles = listOf(EsdtSpecialRole.ESDTRoleLocalMint),
//            action = SetSpecialRolesEsdtUsecase.Action.Set
//        )
    }

    companion object {
        const val TAG = "ShowcaseEsdtIssuanceUsecase"
    }

}