package com.elrond.erdkotlin.domain.esdt

import com.elrond.erdkotlin.domain.wallet.models.Address
import java.math.BigInteger

internal object EsdtConstants {

    const val ESDT_MANAGEMENT_GAS_LIMIT = 60000000L
    val ESDT_TRANSACTION_VALUE = BigInteger.ZERO
    val ESDT_SC_ADDR = Address.fromBech32("erd1qqqqqqqqqqqqqqqpqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqzllls8a5w6u")
    val ESDT_ISSUING_COST = "50000000000000000".toBigInteger() // 0.05 EGLD

    const val GET_TOKEN_PROPERTIES = "getTokenProperties"
    const val GET_SPECIAL_ROLES = "getSpecialRoles"

}
