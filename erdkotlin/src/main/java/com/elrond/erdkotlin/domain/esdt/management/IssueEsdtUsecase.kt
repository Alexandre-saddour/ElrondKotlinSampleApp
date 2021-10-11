package com.elrond.erdkotlin.domain.esdt.management

import com.elrond.erdkotlin.domain.account.models.Account
import com.elrond.erdkotlin.domain.esdt.EsdtConstants
import com.elrond.erdkotlin.domain.esdt.models.ManagementProperty
import com.elrond.erdkotlin.domain.esdt.utils.ValidateTokenNameAndTickerUsecase
import com.elrond.erdkotlin.domain.networkconfig.models.NetworkConfig
import com.elrond.erdkotlin.domain.sc.ScUtils
import com.elrond.erdkotlin.domain.transaction.SendTransactionUsecase
import com.elrond.erdkotlin.domain.transaction.models.Transaction
import com.elrond.erdkotlin.domain.wallet.models.Wallet
import com.elrond.erdkotlin.utils.toHex
import com.elrond.erdkotlin.utils.toHexString
import java.math.BigInteger

class IssueEsdtUsecase internal constructor(
    private val validateTokenNameAndTickerUsecase: ValidateTokenNameAndTickerUsecase,
    private val sendTransactionUsecase: SendTransactionUsecase
) {

    fun execute(
        account: Account,
        wallet: Wallet,
        networkConfig: NetworkConfig,
        gasPrice: Long,
        tokenName: String,
        tokenTicker: String,
        initialSupply: BigInteger,
        numberOfDecimal: Int,
        canFreeze: Boolean? = null,
        canWipe: Boolean? = null,
        canPause: Boolean? = null,
        canMint: Boolean? = null,
        canBurn: Boolean? = null,
        canChangeOwner: Boolean? = null,
        canUpgrade: Boolean? = null,
        canAddSpecialRoles: Boolean? = null
    ) {
        execute(
            account = account,
            wallet = wallet,
            networkConfig = networkConfig,
            gasPrice = gasPrice,
            tokenName = tokenName,
            tokenTicker = tokenTicker,
            initialSupply = initialSupply,
            numberOfDecimal = numberOfDecimal,
            managementProperties = mutableMapOf<ManagementProperty, Boolean>().apply {
                if (canFreeze != null) {
                    put(ManagementProperty.CanFreeze, canFreeze)
                }
                if (canWipe != null) {
                    put(ManagementProperty.CanWipe, canWipe)
                }
                if (canPause != null) {
                    put(ManagementProperty.CanPause, canPause)
                }
                if (canMint != null) {
                    put(ManagementProperty.CanMint, canMint)
                }
                if (canBurn != null) {
                    put(ManagementProperty.CanBurn, canBurn)
                }
                if (canChangeOwner != null) {
                    put(ManagementProperty.CanChangeOwner, canChangeOwner)
                }
                if (canUpgrade != null) {
                    put(ManagementProperty.CanUpgrade, canUpgrade)
                }
                if (canAddSpecialRoles != null) {
                    put(ManagementProperty.CanAddSpecialRoles, canAddSpecialRoles)
                }
            }
        )
    }

    fun execute(
        account: Account,
        wallet: Wallet,
        networkConfig: NetworkConfig,
        gasPrice: Long,
        tokenName: String,
        tokenTicker: String,
        initialSupply: BigInteger,
        numberOfDecimal: Int,
        managementProperties: Map<ManagementProperty, Boolean> = emptyMap()
    ): Transaction {
        // validate parameters
        validateParameters(tokenName, tokenTicker, numberOfDecimal, managementProperties)

        // prepare transaction arguments.
        val args = mutableListOf(
            tokenName.toHex(),
            tokenTicker.toHex(),
            initialSupply.toHex(),
            numberOfDecimal.toHexString()
        ).apply {
            if (managementProperties.isNotEmpty()) {
                addAll(managementProperties.map { (key, value) ->
                    ScUtils.prepareBooleanArgument(key.serializedName, value)
                })
            }
        }

        // send it
        return sendTransactionUsecase.execute(
            Transaction(
                sender = account.address,
                receiver = EsdtConstants.ESDT_SC_ADDR,
                value = EsdtConstants.ESDT_ISSUING_COST,
                gasLimit = EsdtConstants.ESDT_MANAGEMENT_GAS_LIMIT,
                gasPrice = gasPrice,
                data = args.fold("issue") { it1, it2 -> "$it1@$it2" },
                chainID = networkConfig.chainID,
                nonce = account.nonce,
            ),
            wallet
        )
    }

    fun validateParameters(
        tokenName: String,
        tokenTicker: String,
        numberOfDecimal: Int,
        managementProperties: Map<ManagementProperty, Boolean> = emptyMap()
    ) {
        validateTokenNameAndTickerUsecase.execute(tokenName = tokenName, tokenTicker = tokenTicker)
        if (numberOfDecimal < 0 || numberOfDecimal > 18) {
            throw IllegalArgumentException("numberOfDecimal should be between 0 and 18")
        }
        val unsupportedProperties = managementProperties.map { it.key }.filterNot { property ->
            listOfSupportedProperties.contains(property)
        }
        if (unsupportedProperties.isNotEmpty()){
            throw IllegalArgumentException("Found unsupported properties : $unsupportedProperties")
        }
    }

    companion object {
        private val listOfSupportedProperties = listOf(
            ManagementProperty.CanPause,
            ManagementProperty.CanFreeze,
            ManagementProperty.CanWipe,
            ManagementProperty.CanMint,
            ManagementProperty.CanBurn,
            ManagementProperty.CanAddSpecialRoles,
            ManagementProperty.CanChangeOwner,
            ManagementProperty.CanUpgrade,
        )
    }

}
