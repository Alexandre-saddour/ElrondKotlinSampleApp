package com.elrond.erdkotlin.domain.esdt

import com.elrond.erdkotlin.domain.wallet.models.Address

class GetAllRolesForTokensUsecase internal constructor(
    private val esdtRepository: EsdtRepository
) {

    fun execute(address: Address): HashMap<String, List<String>> {
        return esdtRepository.getAllRolesForTokens(address = address)
    }

}