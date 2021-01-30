package com.elrond.erdkotlin.data.networkconfig

import com.elrond.erdkotlin.Exceptions
import com.elrond.erdkotlin.data.api.ElrondProxy
import com.elrond.erdkotlin.data.fromProviderPayload
import com.elrond.erdkotlin.domain.networkconfig.models.NetworkConfig
import com.elrond.erdkotlin.domain.networkconfig.NetworkConfigRepository
import java.io.IOException

internal class NetworkConfigRepositoryImpl(
    private val elrondProxy: ElrondProxy
) : NetworkConfigRepository {

    @Throws(IOException::class, Exceptions.ProxyRequestException::class)
    override fun getNetworkConfig(): NetworkConfig {
        val response = elrondProxy.getNetworkConfig()
        val payload = requireNotNull(response.data).config
        return NetworkConfig.fromProviderPayload(payload)
    }

}
