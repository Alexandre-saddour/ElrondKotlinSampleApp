package com.elrond.erdkotlin

import com.elrond.erdkotlin.helper.TestDataProvider.account
import com.elrond.erdkotlin.helper.TestDataProvider.networkConfig
import com.elrond.erdkotlin.helper.TestDataProvider.wallet
import com.elrond.erdkotlin.domain.vm.CallContractUsecase
import com.elrond.erdkotlin.domain.wallet.models.Address
import com.elrond.erdkotlin.helper.TestUsecaseProvider.sendTransactionUsecase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class CallContractUsecaseTest {

    private val callContractUsecase = CallContractUsecase(sendTransactionUsecase)

    @Test
    fun `should format data correctly`() {
        val sentTransaction = callContractUsecase.execute(
            account = account,
            wallet = wallet,
            networkConfig = networkConfig,
            gasPrice = 100,
            gasLimit = 100,
            contractAddress = Address.fromBech32("erd1qqqqqqqqqqqqqpgqagvtnqn9dgnx7a6stw4n92kufathjrfd8tzqf80mkz"),
            funcName = "awesomeFunc",
            args = listOf("255", "0x5745474c442d616263646566", "0xDEADBEEF")
        )

        assertEquals(
            sentTransaction.data,
            "awesomeFunc@FF@5745474C442D616263646566@DEADBEEF"
        )
    }

    @Test
    fun `should fail if arg is not digit`() {
        assertThrows(IllegalArgumentException::class.java) {
            callContractUsecase.execute(
                account = account,
                wallet = wallet,
                networkConfig = networkConfig,
                gasPrice = 100,
                gasLimit = 100,
                contractAddress = Address.fromBech32("erd1qqqqqqqqqqqqqpgqagvtnqn9dgnx7a6stw4n92kufathjrfd8tzqf80mkz"),
                funcName = "awesomeFunc",
                args = listOf("notADigit")
            )
        }
    }
}