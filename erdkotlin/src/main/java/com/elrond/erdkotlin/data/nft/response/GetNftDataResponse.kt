package com.elrond.erdkotlin.data.nft.response

internal data class GetNftDataResponse(
    val tokenData: NftData
) {
    internal data class NftData(
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
}
