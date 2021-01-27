package com.elrond.erdkotlin.data.networkconfig

import com.elrond.erdkotlin.Exceptions
import com.elrond.erdkotlin.data.api.ElrondService
import com.elrond.erdkotlin.data.fromProviderPayload
import com.elrond.erdkotlin.domain.networkconfig.NetworkConfig
import com.elrond.erdkotlin.domain.networkconfig.NetworkConfigRepository
import java.io.IOException

internal class NetworkConfigRepositoryImpl internal constructor(
    private val elrondService: ElrondService
) : NetworkConfigRepository {

    @Throws(IOException::class, Exceptions.ProxyRequestException::class)
    override fun getNetworkConfig(): NetworkConfig {
        val response = elrondService.getNetworkConfig()
        val payload = requireNotNull(response.data).config
        return NetworkConfig.fromProviderPayload(payload)
    }

}