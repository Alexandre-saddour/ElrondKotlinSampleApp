package fr.asaddour.elrondkotlinsdk.domain.showcase

import com.elrond.erdkotlin.domain.transaction.GetTransactionInfoUsecase
import com.elrond.erdkotlin.domain.transaction.models.TransactionInfo
import com.elrond.erdkotlin.domain.wallet.models.Address
import kotlinx.coroutines.delay
import java.lang.Exception
import javax.inject.Inject

class PollTransactionUsecase @Inject constructor(
    private val getTransactionInfoUsecase: GetTransactionInfoUsecase
) {

    suspend fun execute(
        txHash: String,
        sender: Address? = null,
        withResults: Boolean = false,
        times: Int = Int.MAX_VALUE,
        initialDelay: Long = 100, // 0.1 second
        maxDelay: Long = 3000,    // 2 second
        factor: Double = 2.0,
        predicate: (transactionInfo: TransactionInfo) -> Boolean
    ) = retry(times = times, initialDelay = initialDelay, maxDelay = maxDelay, factor = factor) {
        val infos = getTransactionInfoUsecase.execute(
            txHash = txHash,
            sender = sender,
            withResults = withResults,
        )
        Result(predicate(infos), infos)
    }

    private suspend fun <T> retry(
        times: Int = Int.MAX_VALUE,
        initialDelay: Long = 100, // 0.1 second
        maxDelay: Long = 1000,    // 1 second
        factor: Double = 2.0,
        block: suspend () -> Result<T>): T
    {
        var currentDelay = initialDelay
        repeat(times - 1) {
            try {
                val result = block()
                if (result.success){
                    return result.data
                }
            } catch (e: Exception) {
                // no-op
            }
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
        }
        return block().data // last attempt
    }

    private data class Result<T>(
        val success: Boolean,
        val data: T
    )

}