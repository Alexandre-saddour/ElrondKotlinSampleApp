package com.elrond.erdkotlin.domain.transaction

import com.elrond.erdkotlin.Exceptions
import com.elrond.erdkotlin.domain.wallet.Address
import java.io.IOException

internal interface TransactionRepository {

    @Throws(
        IOException::class,
        Exceptions.CannotSerializeTransactionException::class,
        Exceptions.ProxyRequestException::class
    )
    fun sendTransaction(transaction: Transaction): TransactionHash

    @Throws(IOException::class)
    fun getTransactions(address: Address): List<TransactionOnNetwork>

}