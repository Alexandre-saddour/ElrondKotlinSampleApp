package com.elrond.erdkotlin.domain.esdt

import com.elrond.erdkotlin.domain.esdt.models.EsdtSpecialRole
import com.elrond.erdkotlin.domain.wallet.models.Address

class GetTokensWithRoleUsecase internal constructor(
    private val esdtRepository: EsdtRepository
) {

    fun execute(address: Address, role: EsdtSpecialRole): List<String> {
        return esdtRepository.getTokensWithRole(address = address, role = role)
    }

}