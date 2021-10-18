package fr.asaddour.elrondkotlinsdk.domain.showcase

import android.util.Log
import com.elrond.erdkotlin.utils.hexToString
import javax.inject.Inject

class PollNftTokenIdentifierUsecase @Inject constructor(
    private val pollTransactionUsecase: PollTransactionUsecase
) {

    suspend fun execute(txHash: String, withResults: Boolean): String? {
        val infos = pollTransactionUsecase.execute(
            txHash = txHash,
            withResults = withResults,
            initialDelay = 1000,
            maxDelay = 30_000,
        ) { infos ->
            Log.d("alex", "infos:$infos")
            infos.smartContractResults?.isNotEmpty() ?: false
        }
        // This should look like "@ok@414c432d317132773365"
        val data = infos.smartContractResults?.firstOrNull()?.data
        val tokenIdentifier = data?.split("@")?.getOrNull(2)
        return tokenIdentifier?.hexToString()
    }

}