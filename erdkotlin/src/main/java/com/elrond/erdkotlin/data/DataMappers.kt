package com.elrond.erdkotlin.data

import com.elrond.erdkotlin.data.account.responses.GetAccountResponse
import com.elrond.erdkotlin.data.account.responses.GetAddressTransactionsResponse
import com.elrond.erdkotlin.data.networkconfig.GetNetworkConfigResponse
import com.elrond.erdkotlin.data.transaction.responses.GetTransactionInfoResponse
import com.elrond.erdkotlin.domain.networkconfig.models.NetworkConfig
import com.elrond.erdkotlin.domain.account.models.Account
import com.elrond.erdkotlin.domain.transaction.models.TransactionInfo
import com.elrond.erdkotlin.domain.transaction.models.TransactionOnNetwork
import com.elrond.erdkotlin.domain.wallet.models.Address

internal fun GetAccountResponse.AccountData.toDomain(address: Address) = Account(
    address = address,
    nonce = nonce,
    balance = balance,
)

internal fun GetAddressTransactionsResponse.TransactionOnNetworkData.toDomain() =
    TransactionOnNetwork(
        sender = Address.fromBech32(sender),
        receiver = Address.fromBech32(receiver),
        nonce = nonce,
        value = value,
        gasPrice = gasPrice,
        gasLimit = gasLimit,
        signature = signature,
        hash = hash,
        data = data,
        status = status,
        timestamp = timestamp,
        gasUsed = gasUsed,
        receiverShard = receiverShard,
        senderShard = senderShard,
        miniBlockHash = miniBlockHash,
        round = round,
        searchOrder = searchOrder,
        fee = fee,
        scResults = scResults
    )

internal fun GetTransactionInfoResponse.TransactionInfoData.toDomain() = TransactionInfo(
    type = type,
    nonce = nonce,
    round = round,
    epoch = epoch,
    value = value,
    sender = Address.fromBech32(sender),
    receiver = Address.fromBech32(receiver),
    gasPrice = gasPrice,
    gasLimit = gasLimit,
    data = data,
    signature = signature,
    sourceShard = sourceShard,
    destinationShard = destinationShard,
    blockNonce = blockNonce,
    miniBlockHash = miniBlockHash,
    blockHash = blockHash,
    status = status
)

internal fun NetworkConfig.Companion.fromProviderPayload(
    response: GetNetworkConfigResponse.NetworkConfigData
) = NetworkConfig(
    chainID = response.chainID,
    gasPerDataByte = response.gasPerDataByte,
    minGasLimit = response.minGasLimit,
    minGasPrice = response.minGasPrice,
    minTransactionVersion = response.minTransactionVersion
)
