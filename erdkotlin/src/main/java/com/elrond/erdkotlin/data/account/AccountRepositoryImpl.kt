package com.elrond.erdkotlin.data.account

import com.elrond.erdkotlin.Exceptions
import com.elrond.erdkotlin.data.api.ElrondService
import com.elrond.erdkotlin.data.toDomain
import com.elrond.erdkotlin.domain.wallet.Address
import com.elrond.erdkotlin.domain.account.Account
import com.elrond.erdkotlin.domain.account.AccountRepository
import java.io.IOException

internal class AccountRepositoryImpl internal constructor(
    private val elrondService: ElrondService
) : AccountRepository {

    @Throws(
        IOException::class,
        Exceptions.AddressException::class,
        Exceptions.ProxyRequestException::class
    )
    override fun getAccount(address: Address): Account {
        val response = elrondService.getAccount(address)
        val payload = requireNotNull(response.data).account
        return payload.toDomain(address)
    }
}