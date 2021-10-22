package com.elrond.erdkotlin.domain.nft

import com.elrond.erdkotlin.domain.wallet.models.Address

class GetNftsRegisteredUsecase internal constructor(
    private val nftRepository: NftRepository
) {

    fun execute(address: Address): List<String> {
        return nftRepository.getNftsRegistered(address)
    }
}
