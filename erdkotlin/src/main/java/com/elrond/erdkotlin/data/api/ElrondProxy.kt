package com.elrond.erdkotlin.data.api

import com.elrond.erdkotlin.data.account.responses.GetAccountResponse
import com.elrond.erdkotlin.data.account.responses.GetAddressBalanceResponse
import com.elrond.erdkotlin.data.account.responses.GetAddressNonceResponse
import com.elrond.erdkotlin.data.networkconfig.GetNetworkConfigResponse
import com.elrond.erdkotlin.data.transaction.responses.EstimateCostOfTransactionResponse
import com.elrond.erdkotlin.data.transaction.responses.GetTransactionInfoResponse
import com.elrond.erdkotlin.data.transaction.responses.GetTransactionStatusResponse
import com.elrond.erdkotlin.data.transaction.responses.SendTransactionResponse
import com.elrond.erdkotlin.domain.transaction.models.TransactionToEstimate
import com.elrond.erdkotlin.domain.transaction.models.Transaction
import com.elrond.erdkotlin.domain.transaction.models.TransactionOnNetwork
import com.elrond.erdkotlin.domain.wallet.models.Address
import com.google.gson.Gson

internal class ElrondProxy(
    url: String
) {

    private val gson = Gson()
    private val elrondClient = ElrondClient(url, gson)

    fun setUrl(url: String) {
        elrondClient.url = url
    }

    fun getNetworkConfig(): ElrondClient.ResponseBase<GetNetworkConfigResponse> {
        return elrondClient.doGet("network/config")
    }

    // Addresses

    fun getAccount(address: Address): ElrondClient.ResponseBase<GetAccountResponse> {
        return elrondClient.doGet("address/${address.bech32()}")
    }

    fun getAddressNonce(address: Address): ElrondClient.ResponseBase<GetAddressNonceResponse> {
        return elrondClient.doGet("address/${address.bech32()}/nonce")
    }

    fun getAddressBalance(address: Address): ElrondClient.ResponseBase<GetAddressBalanceResponse> {
        return elrondClient.doGet("address/${address.bech32()}/balance")
    }

    fun getAddressTransactions(address: Address): ElrondClient.ResponseBase<List<TransactionOnNetwork>> {
        return elrondClient.doGet("address/${address.bech32()}/transactions")
    }

    // Transactions

    fun sendTransaction(transaction: Transaction): ElrondClient.ResponseBase<SendTransactionResponse> {
        val requestJson = transaction.serialize()
        return elrondClient.doPost("transaction/send", requestJson)
    }

    fun estimateCostOfTransaction(transaction: TransactionToEstimate): ElrondClient.ResponseBase<EstimateCostOfTransactionResponse> {
        return elrondClient.doPost("transaction/cost", gson.toJson(transaction))
    }

    fun getTransactionInfo(txHash: String, sender: Address?): ElrondClient.ResponseBase<GetTransactionInfoResponse> {
        val senderAddress = when (sender){
            null -> ""
            else -> "?sender=${sender.bech32()}"
        }
        return elrondClient.doGet("transaction/$txHash$senderAddress")
    }

    fun getTransactionStatus(txHash: String, sender: Address?): ElrondClient.ResponseBase<GetTransactionStatusResponse> {
        val senderAddress = when (sender){
            null -> ""
            else -> "?sender=${sender.bech32()}"
        }
        return elrondClient.doGet("transaction/$txHash/status$senderAddress")
    }

}
