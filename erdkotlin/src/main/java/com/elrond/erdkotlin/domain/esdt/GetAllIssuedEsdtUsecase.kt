package com.elrond.erdkotlin.domain.esdt

import com.elrond.erdkotlin.domain.esdt.models.EsdtTokenType

class GetAllIssuedEsdtUsecase internal constructor(private val esdtRepository: EsdtRepository) {

    fun execute(type: EsdtTokenType) = esdtRepository.getAllTokenIssued(type)

}
