package fr.asaddour.elrondkotlinsdk.domain.transaction

import com.elrond.erdkotlin.domain.transaction.GetTransactionStatusUsecase
import fr.asaddour.elrondkotlinsdk.extentions.retry
import javax.inject.Inject

class PollTransactionStatusUsecase @Inject constructor(
    private val getTransactionStatusUsecase: GetTransactionStatusUsecase
) {

    // Polling because fetching the status can throw {"code":"internal_issue","error":"transaction not found"}
    suspend fun execute(txHash: String) = retry(initialDelay = 300) {
        getTransactionStatusUsecase.execute(txHash)
    }
}