package com.elrond.erdkotlin.data.nft

import com.elrond.erdkotlin.data.api.ElrondProxy
import com.elrond.erdkotlin.data.toDomain
import com.elrond.erdkotlin.domain.nft.NftRepository
import com.elrond.erdkotlin.domain.nft.models.NftData
import com.elrond.erdkotlin.domain.wallet.models.Address

internal class NftRepositoryImpl(
    private val elrondProxy: ElrondProxy
) : NftRepository {

    override fun getNftData(address: Address, tokenIdentifier: String, nonce: Long): NftData {
        val data = requireNotNull(elrondProxy.getNftData(address, tokenIdentifier, nonce).data)
        return data.toDomain()
    }

    override fun getNftsRegistered(address: Address): List<String> {
        return requireNotNull(elrondProxy.getNftsRegistered(address).data).tokens
    }
}
