package com.elrond.erdkotlin.domain.transaction

import com.elrond.erdkotlin.domain.wallet.Address
import java.math.BigInteger

data class TransactionOnNetwork(
    val sender: Address,
    val receiver: Address,
    val nonce: Long,
    val value: BigInteger,
    val gasPrice: Long,
    val gasLimit: Long,
    val signature: String,
    val hash: String,
    val data: String?,
    val status: String,
    val timestamp: Long,
    val gasUsed: Long,
    val receiverShard: Long,
    val senderShard: Long,
    val miniBlockHash: String,
    val round: Long,
    val searchOrder: Long,
    val fee: String,
    val scResults: List<ScResult>?,
) {

    data class ScResult(
        val hash: String,
        val nonce: Long,
        val gasLimit: Long,
        val gasPrice: Long,
        val value: String,
        val sender: String,
        val receiver: String,
        val relayedValue: String,
        val data: String,
        val prevTxHash: String,
        val originalTxHash: String,
        val callType: String,
    )

}