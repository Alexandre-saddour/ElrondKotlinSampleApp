package com.elrond.erdkotlin.domain.esdt

class GetAllIssuedEsdtUsecase internal constructor(private val esdtRepository: EsdtRepository) {

    fun execute(type: Type) = esdtRepository.getAllTokenIssued(type)

    enum class Type {
        ESDT,
        Fungible,
        SFT,
        NFT
    }
}
