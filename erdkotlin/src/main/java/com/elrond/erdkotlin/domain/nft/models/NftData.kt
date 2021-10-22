package com.elrond.erdkotlin.domain.nft.models

data class NftData(
    val balance: String,
    val tokenIdentifier: String,
    val attributes: String?,
    val creator: String?,
    val hash: String?,
    val name: String?,
    val nonce: Int?,
    val royalties: String?,
    val uris: List<String>?
)
