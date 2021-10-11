package com.elrond.erdkotlin.domain.nft

import com.elrond.erdkotlin.domain.account.models.Account
import com.elrond.erdkotlin.domain.esdt.EsdtConstants
import com.elrond.erdkotlin.domain.networkconfig.models.NetworkConfig
import com.elrond.erdkotlin.domain.transaction.SendTransactionUsecase
import com.elrond.erdkotlin.domain.transaction.models.Transaction
import com.elrond.erdkotlin.domain.wallet.models.Wallet
import com.elrond.erdkotlin.utils.toHex
import com.elrond.erdkotlin.utils.toHexString
import java.math.BigInteger

class CreateNftUsecase internal constructor(
    private val sendTransactionUsecase: SendTransactionUsecase
) {

    fun execute(
        account: Account,
        wallet: Wallet,
        networkConfig: NetworkConfig,
        gasPrice: Long,
        tokenIdentifier: String,
        initialQuantity: BigInteger, // The quantity of the token. If NFT, it must be 1
        nftName: String,
        royalties: Int,
        uris: List<String>,
        hash: String? = null,
        attributes: String? = null
    ): Transaction {
        // validate parameters
        if (uris.isEmpty()) {
            throw IllegalArgumentException("uris must contains at least one entry")
        }
        if (royalties !in 0..10000) {
            throw IllegalArgumentException("royalties must be between 0 and 10000")
        }

        // prepare transaction arguments.
        val args = mutableListOf(
            tokenIdentifier.toHex(),
            initialQuantity.toHex(),
            nftName.toHex(),
            royalties.toHexString()
        ).apply {
            hash?.let { add(it.toHex()) }
            attributes?.let { add(it.toHex()) }
            addAll(uris.map { it.toHex() })
        }
        val data = args.fold("ESDTNFTCreate") { it1, it2 -> "$it1@$it2" }
        val extraGasLimit: Long = run {
            // Transaction payload cost: Data field length * 1500 (GasPerDataByte = 1500)
            // Storage cost: Size of NFT data * 50000 (StorePerByte = 50000)
            val transactionCost = data.length.toLong() * networkConfig.gasPerDataByte.toLong()
            val storageCost = STORE_PER_BYTE * args.fold(0L) { it1, it2 ->
                it1 + it2.length.toLong()
            }
            transactionCost + storageCost
        }

        // send it
        return sendTransactionUsecase.execute(
            Transaction(
                sender = account.address, // <address with ESDTRoleNFTCreate role>
                receiver = account.address, // <same as sender>
                value = EsdtConstants.ESDT_TRANSACTION_VALUE,
                gasLimit = EsdtConstants.ESDT_MANAGEMENT_GAS_LIMIT + extraGasLimit,
                gasPrice = gasPrice,
                data = data,
                chainID = networkConfig.chainID,
                nonce = account.nonce,
            ),
            wallet
        )
    }

    companion object {
        private const val STORE_PER_BYTE = 50000L
    }
}