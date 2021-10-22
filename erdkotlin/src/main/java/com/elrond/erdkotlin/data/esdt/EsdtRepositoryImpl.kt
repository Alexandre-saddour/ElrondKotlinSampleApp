package com.elrond.erdkotlin.data.esdt

import com.elrond.erdkotlin.data.api.ElrondProxy
import com.elrond.erdkotlin.data.toEsdtProperties
import com.elrond.erdkotlin.data.toSpecialRoles
import com.elrond.erdkotlin.domain.esdt.EsdtConstants
import com.elrond.erdkotlin.domain.esdt.EsdtRepository
import com.elrond.erdkotlin.domain.esdt.GetAllIssuedEsdtUsecase
import com.elrond.erdkotlin.domain.esdt.models.*
import com.elrond.erdkotlin.domain.vm.VmRepository
import com.elrond.erdkotlin.domain.vm.query.QueryContractInput
import com.elrond.erdkotlin.domain.wallet.models.Address
import com.elrond.erdkotlin.utils.toHex

internal class EsdtRepositoryImpl(
    private val elrondProxy: ElrondProxy,
    private val vmRepository: VmRepository
) : EsdtRepository {

    override fun getEsdtTokens(address: Address): Map<String, EsdtToken> {
        return requireNotNull(elrondProxy.getEsdtTokens(address).data).esdts
    }

    override fun getEsdtBalance(address: Address, tokenIdentifier: String): EsdtTokenBalance {
        return requireNotNull(elrondProxy.getEsdtBalance(address, tokenIdentifier).data).tokenData
    }

    override fun getAllTokenIssued(type: EsdtTokenType): List<String> {
        return requireNotNull(elrondProxy.getAllTokenIssued(type).data).tokens
    }

    override fun getEsdtProperties(tokenIdentifier: String): EsdtProperties {
        val response = vmRepository.queryContract(
            QueryContractInput(
                scAddress = EsdtConstants.ESDT_SC_ADDR.bech32,
                funcName = EsdtConstants.GET_TOKEN_PROPERTIES,
                args = listOf(tokenIdentifier.toHex())
            )
        )
        return response.toEsdtProperties()
    }

    override fun getEsdtSpecialRoles(tokenIdentifier: String): EsdtSpecialRoles? {
        val response = vmRepository.queryContract(
            QueryContractInput(
                scAddress = EsdtConstants.ESDT_SC_ADDR.bech32,
                funcName = EsdtConstants.GET_SPECIAL_ROLES,
                args = listOf(tokenIdentifier.toHex())
            )
        )
        return response.toSpecialRoles()
    }

    override fun getAllRolesForTokens(address: Address): HashMap<String, List<String>> {
        return requireNotNull(elrondProxy.getAllRolesForTokens(address).data).roles
    }

    override fun getTokensWithRole(address: Address, role: EsdtSpecialRole): List<String> {
        return requireNotNull(elrondProxy.getTokensWithRole(address, role).data).tokens
    }

}
