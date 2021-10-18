package com.elrond.erdkotlin.utils

import com.elrond.erdkotlin.domain.esdt.utils.ValidateTokenNameAndTickerUsecase
import org.junit.Assert
import org.junit.Test

class ValidateTokenNameAndTickerUsecaseTest {

    private val usecase = ValidateTokenNameAndTickerUsecase()

    @Test
    fun `tokenName name length should not be lower than 3`() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            usecase.execute(
                tokenName = "a",
                tokenTicker = "EKT"
            )
        }
    }

    @Test
    fun `tokenName name length should not be bigger than 20`() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            usecase.execute(
                tokenName = "abcefghijklmnopqrstuv",
                tokenTicker = "EKT"
            )
        }
    }

    @Test
    fun `tokenName name should be alphanumeric`() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            usecase.execute(
                tokenName = "abc-efg",
                tokenTicker = "EKT"
            )
        }
    }

    @Test
    fun `tokenTicker name length should not be lower than 3`() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            usecase.execute(
                tokenName = "abcde",
                tokenTicker = "E"
            )
        }
    }

    @Test
    fun `tokenTicker name length should not be bigger than 10`() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            usecase.execute(
                tokenName = "abc",
                tokenTicker = "ABCDEFGHIJK"
            )
        }
    }

    @Test
    fun `tokenTicker name should be uppercase`() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            usecase.execute(
                tokenName = "abcefg",
                tokenTicker = "ekt"
            )
        }
    }

    @Test
    fun `tokenTicker name should be alphanumeric`() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            usecase.execute(
                tokenName = "abcefg",
                tokenTicker = "EK-T"
            )
        }
    }

}