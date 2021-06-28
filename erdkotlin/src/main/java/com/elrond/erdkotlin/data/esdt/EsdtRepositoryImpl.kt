package com.elrond.erdkotlin.data.esdt

import com.elrond.erdkotlin.data.api.ElrondProxy
import com.elrond.erdkotlin.domain.esdt.EsdtRepository
import com.elrond.erdkotlin.domain.esdt.models.EsdtTokenBalance
import com.elrond.erdkotlin.domain.esdt.models.EsdtToken
import com.elrond.erdkotlin.domain.wallet.models.Address

internal class EsdtRepositoryImpl(
    private val elrondProxy: ElrondProxy
) : EsdtRepository {

    override fun getEsdtTokens(address: Address): Map<String, EsdtToken> {
        return requireNotNull(elrondProxy.getEsdtTokens(address).data).esdts
    }

    override fun getEsdtBalance(address: Address, tokenIdentifier: String): EsdtTokenBalance {
        return requireNotNull(elrondProxy.getEsdtBalance(address, tokenIdentifier).data).tokenData
    }

    override fun getAllEsdtIssued(): List<String> {
        return requireNotNull(elrondProxy.getAllIssuedEsdt().data).tokens
    }

    override fun getEsdtProperties(tokenIdentifier: String): Any {
        TODO("Not yet implemented")
    }

    override fun getEsdtSpecialRoles(tokenIdentifier: String): Any {
        TODO("Not yet implemented")
    }
}