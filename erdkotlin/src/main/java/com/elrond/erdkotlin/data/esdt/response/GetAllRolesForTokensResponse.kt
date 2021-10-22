package com.elrond.erdkotlin.data.esdt.response

internal data class GetAllRolesForTokensResponse(
    val roles: HashMap<String, List<String>>
)
