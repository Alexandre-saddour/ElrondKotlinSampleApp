package com.elrond.erdkotlin.data.transaction

import com.elrond.erdkotlin.Exceptions
import com.elrond.erdkotlin.data.api.ElrondService
import com.elrond.erdkotlin.domain.transaction.Transaction
import com.elrond.erdkotlin.domain.transaction.TransactionHash
import com.elrond.erdkotlin.domain.transaction.TransactionOnNetwork
import com.elrond.erdkotlin.domain.transaction.TransactionRepository
import com.elrond.erdkotlin.domain.wallet.Address
import java.io.IOException

internal class TransactionRepositoryImpl internal constructor(
    private val elrondService: ElrondService
) : TransactionRepository {
    @Throws(
        IOException::class,
        Exceptions.CannotSerializeTransactionException::class,
        Exceptions.ProxyRequestException::class
    )
    override fun sendTransaction(transaction: Transaction): TransactionHash {
        val response = elrondService.sendTransaction(transaction)
        return TransactionHash(requireNotNull(response.data).txHash)
    }

    override fun getTransactions(address: Address): List<TransactionOnNetwork> {
        val response = elrondService.getAddressTransactions(address)
        return requireNotNull(response.data)
    }
}