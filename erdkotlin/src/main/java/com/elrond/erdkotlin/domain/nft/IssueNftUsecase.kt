package com.elrond.erdkotlin.domain.nft

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

class IssueNftUsecase internal constructor(
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
        type: Type,
        canFreeze: Boolean? = null,
        canWipe: Boolean? = null,
        canPause: Boolean? = null,
        canTransferNFTCreateRole: Boolean? = null
    ) = execute(
        account = account,
        wallet = wallet,
        networkConfig = networkConfig,
        gasPrice = gasPrice,
        tokenName = tokenName,
        tokenTicker = tokenTicker,
        type = type,
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
            if (canTransferNFTCreateRole != null) {
                put(ManagementProperty.CanTransferNFTCreateRole, canTransferNFTCreateRole)
            }
        }
    )

    fun execute(
        account: Account,
        wallet: Wallet,
        networkConfig: NetworkConfig,
        gasPrice: Long,
        tokenName: String,
        tokenTicker: String,
        type: Type,
        managementProperties: Map<ManagementProperty, Boolean> = emptyMap()
    ): Transaction {
        validateParameters(tokenName, tokenTicker, managementProperties)

        // prepare transaction arguments.
        val args = mutableListOf(
            tokenName.toHex(),
            tokenTicker.toHex()
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
                data = args.fold(type.funcName) { it1, it2 -> "$it1@$it2" },
                chainID = networkConfig.chainID,
                nonce = account.nonce,
            ),
            wallet
        )
    }

    private fun validateParameters(
        tokenName: String,
        tokenTicker: String,
        managementProperties: Map<ManagementProperty, Boolean> = emptyMap()
    ) {
        run {
            validateTokenNameAndTickerUsecase.execute(tokenName = tokenName, tokenTicker = tokenTicker)
            val unsupportedProperties = managementProperties.map { it.key }.filterNot { property ->
                listOfSupportedProperties.contains(property)
            }
            if (unsupportedProperties.isNotEmpty()){
                throw IllegalArgumentException("Found unsupported properties : $unsupportedProperties")
            }
        }
    }

    enum class Type(val funcName: String) {
        NFT(funcName = "issueNonFungible"),
        SFT(funcName = "issueSemiFungible")
    }

    companion object {
        private val listOfSupportedProperties = listOf(
            ManagementProperty.CanPause,
            ManagementProperty.CanFreeze,
            ManagementProperty.CanWipe,
            ManagementProperty.CanTransferNFTCreateRole
        )
    }
}
