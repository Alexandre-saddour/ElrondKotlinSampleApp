package fr.asaddour.elrondkotlinsdk.domain.transaction

import com.elrond.erdkotlin.domain.transaction.GetTransactionInfoUsecase
import fr.asaddour.elrondkotlinsdk.utils.ext.retry
import javax.inject.Inject

class PollTransactionInfoUsecase @Inject constructor(
    private val getTransactionInfoUsecase: GetTransactionInfoUsecase
) {

    // Polling because fetching the info can throw {"code":"internal_issue","error":"transaction not found"}
    suspend fun execute(txHash: String) = retry(initialDelay = 300) {
        getTransactionInfoUsecase.execute(txHash)
    }
}