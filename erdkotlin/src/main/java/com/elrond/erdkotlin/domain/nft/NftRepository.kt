package com.elrond.erdkotlin.domain.nft

import com.elrond.erdkotlin.domain.nft.models.NftData
import com.elrond.erdkotlin.domain.wallet.models.Address

internal interface NftRepository {

    fun getNftData(
        address: Address,
        tokenIdentifier: String,
        nonce: Long
    ): NftData

    fun getNftsRegistered(
        address: Address
    ): List<String>
}
