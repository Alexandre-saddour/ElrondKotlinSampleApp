package com.elrond.erdkotlin.domain.nft

import com.elrond.erdkotlin.domain.nft.models.NftData
import com.elrond.erdkotlin.domain.wallet.models.Address

class GetNftDataUsecase internal constructor(
    private val nftRepository: NftRepository
) {

    fun execute(
        address: Address,
        tokenIdentifier: String,
        nonce: Long
    ): NftData {
        return nftRepository.getNftData(
            address = address,
            tokenIdentifier = tokenIdentifier,
            nonce = nonce
        )
    }
}
