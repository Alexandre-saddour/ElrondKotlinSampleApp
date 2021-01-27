package com.elrond.erdkotlin.data.api

import com.elrond.erdkotlin.data.account.GetAccountResponse
import com.elrond.erdkotlin.data.networkconfig.GetNetworkConfigResponse
import com.elrond.erdkotlin.data.transaction.TransactionResponse
import com.elrond.erdkotlin.domain.transaction.Transaction
import com.elrond.erdkotlin.domain.transaction.TransactionOnNetwork
import com.elrond.erdkotlin.domain.wallet.Address

internal class ElrondService(private val elrondClient: ElrondClient) {

    fun getNetworkConfig(): ElrondClient.ResponseBase<GetNetworkConfigResponse> {
        return elrondClient.doGet("network/config")
    }

    fun getAccount(address: Address): ElrondClient.ResponseBase<GetAccountResponse> {
        return elrondClient.doGet("address/${address.bech32()}")
    }

    fun getAddressTransactions(address: Address): ElrondClient.ResponseBase<List<TransactionOnNetwork>> {
        return elrondClient.doGet("address/${address.bech32()}/transactions")
    }

    fun sendTransaction(transaction: Transaction): ElrondClient.ResponseBase<TransactionResponse> {
        val requestJson = transaction.serialize()
        return elrondClient.doPost("transaction/send", requestJson)
    }
}