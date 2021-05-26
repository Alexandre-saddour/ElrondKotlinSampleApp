package com.elrond.erdkotlin

import com.elrond.erdkotlin.helper.TestDataProvider.account
import com.elrond.erdkotlin.helper.TestDataProvider.networkConfig
import com.elrond.erdkotlin.helper.TestDataProvider.wallet
import com.elrond.erdkotlin.domain.vm.ExecuteContractUsecase
import com.elrond.erdkotlin.domain.wallet.models.Address
import com.elrond.erdkotlin.helper.TestUsecaseProvider.sendTransactionUsecase
import org.junit.Assert.assertThrows
import org.junit.Test

class ExecuteContractUsecaseTest {

    private val executeContractUsecase = ExecuteContractUsecase(sendTransactionUsecase)

    @Test
    fun `should succeed if arg is digit`() {
        executeContractUsecase.execute(
            account = account,
            wallet = wallet,
            networkConfig = networkConfig,
            gasPrice = 100,
            gasLimit = 100,
            contractAddress = Address.fromBech32("erd1qqqqqqqqqqqqqpgqagvtnqn9dgnx7a6stw4n92kufathjrfd8tzqf80mkz"),
            funcName = "increment",
            args = listOf("255")
        )
    }

    @Test
    fun `should succeed if arg is hex`() {
        executeContractUsecase.execute(
            account = account,
            wallet = wallet,
            networkConfig = networkConfig,
            gasPrice = 100,
            gasLimit = 100,
            contractAddress = Address.fromBech32("erd1qqqqqqqqqqqqqpgqagvtnqn9dgnx7a6stw4n92kufathjrfd8tzqf80mkz"),
            funcName = "increment",
            args = listOf("0xff")
        )
    }


    @Test
    fun `should fail if arg is not digit`() {
        assertThrows(IllegalArgumentException::class.java) {
            executeContractUsecase.execute(
                account = account,
                wallet = wallet,
                networkConfig = networkConfig,
                gasPrice = 100,
                gasLimit = 100,
                contractAddress = Address.fromBech32("erd1qqqqqqqqqqqqqpgqagvtnqn9dgnx7a6stw4n92kufathjrfd8tzqf80mkz"),
                funcName = "increment",
                args = listOf("notADigit")
            )
        }
    }
}