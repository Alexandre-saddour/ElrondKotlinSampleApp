package com.elrond.erdkotlin.data.transaction

import com.elrond.erdkotlin.data.ElrondClient
import com.elrond.erdkotlin.Exceptions
import com.elrond.erdkotlin.domain.transaction.Transaction
import com.elrond.erdkotlin.domain.transaction.TransactionHash
import com.elrond.erdkotlin.domain.transaction.TransactionOnNetwork
import com.elrond.erdkotlin.domain.transaction.TransactionRepository
import com.elrond.erdkotlin.domain.wallet.Address
import java.io.IOException

internal class TransactionRepositoryImpl internal constructor(
    private val elrondClient: ElrondClient
) : TransactionRepository {
    @Throws(
        IOException::class,
        Exceptions.CannotSerializeTransactionException::class,
        Exceptions.ProxyRequestException::class
    )
    override fun sendTransaction(transaction: Transaction): TransactionHash {
        val requestJson = transaction.serialize()
        val response: ElrondClient.ResponseBase<TransactionResponse> = elrondClient.doPost(
            "transaction/send", requestJson
        )
        return TransactionHash(requireNotNull(response.data).txHash)
    }

    override fun getTransactions(address: Address): List<TransactionOnNetwork> {
        val response: ElrondClient.ResponseBase<List<TransactionOnNetwork>> = elrondClient.doGet(
            "address/${address.bech32()}/transactions"
        )
        return requireNotNull(response.data)
    }
}