package com.elrond.erdkotlin.domain.nft

import com.elrond.erdkotlin.domain.account.models.Account
import com.elrond.erdkotlin.domain.esdt.EsdtConstants
import com.elrond.erdkotlin.domain.esdt.EsdtConstants.ESDT_MANAGEMENT_GAS_LIMIT
import com.elrond.erdkotlin.domain.esdt.EsdtConstants.ESDT_SC_ADDR
import com.elrond.erdkotlin.domain.networkconfig.models.NetworkConfig
import com.elrond.erdkotlin.domain.transaction.SendTransactionUsecase
import com.elrond.erdkotlin.domain.transaction.models.Transaction
import com.elrond.erdkotlin.domain.wallet.models.Address
import com.elrond.erdkotlin.domain.wallet.models.Wallet
import com.elrond.erdkotlin.utils.toHex

class TransferNFTCreateRoleUsecase(
    private val sendTransactionUsecase: SendTransactionUsecase
) {

    fun execute(
        account: Account,
        wallet: Wallet,
        networkConfig: NetworkConfig,
        gasPrice: Long,
        tokenIdentifier: String,
        roleReceiver: Address
    ): Transaction {

        // prepare transactions arguments
        val args = mutableListOf(
            tokenIdentifier.toHex(),
            account.address.hex,
            roleReceiver.hex
        )
        val data = args.fold("transferNFTCreateRole") { it1, it2 -> "$it1@$it2" }
        val extraGasLimit = data.length.toLong() * networkConfig.gasPerDataByte.toLong()

        // send it
        return sendTransactionUsecase.execute(
            Transaction(
                sender = account.address, // <address of the current creation role owner>
                receiver = ESDT_SC_ADDR,
                value = EsdtConstants.ESDT_TRANSACTION_VALUE,
                gasLimit = ESDT_MANAGEMENT_GAS_LIMIT + extraGasLimit,
                gasPrice = gasPrice,
                data = data,
                chainID = networkConfig.chainID,
                nonce = account.nonce
            ),
            wallet
        )
    }
}
