package com.elrond.erdkotlin.domain.esdt.models

import com.elrond.erdkotlin.domain.wallet.models.Address

data class EsdtSpecialRoles(
    val address: Map<Address, List<EsdtSpecialRole>>
)
