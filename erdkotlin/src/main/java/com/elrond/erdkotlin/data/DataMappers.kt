package com.elrond.erdkotlin.data

import com.elrond.erdkotlin.data.account.responses.GetAccountResponse
import com.elrond.erdkotlin.data.networkconfig.GetNetworkConfigResponse
import com.elrond.erdkotlin.domain.networkconfig.models.NetworkConfig
import com.elrond.erdkotlin.domain.account.models.Account
import com.elrond.erdkotlin.domain.wallet.models.Address

internal fun GetAccountResponse.AccountData.toDomain(address: Address) = Account(
    address = address,
    nonce = nonce,
    balance = balance,
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