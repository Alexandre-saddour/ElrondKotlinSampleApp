package com.elrond.erdkotlin.domain.esdt.utils

internal class ValidateTokenNameAndTickerUsecase {

    fun execute(tokenName: String, tokenTicker: String) {
        if (!tokenName.matches("^[A-Za-z0-9]{3,20}$".toRegex())) {
            throw IllegalArgumentException(
                "tokenName length should be between 3 and 20 characters " +
                        "and alphanumeric only"
            )
        }
        if (!tokenTicker.matches("^[A-Z0-9]{3,10}$".toRegex())) {
            throw IllegalArgumentException(
                "tokenTicker length should be between 3 and 10 characters " +
                        "and alphanumeric uppercase only"
            )
        }
    }

}