package com.elrond.erdkotlin.domain.esdt

import com.elrond.erdkotlin.domain.esdt.models.*
import com.elrond.erdkotlin.domain.wallet.models.Address

internal interface EsdtRepository {

    // Get all ESDT tokens for an address
    fun getEsdtTokens(address: Address): Map<String, EsdtToken>

    // Get balance for an address and an ESDT token
    fun getEsdtBalance(address: Address, tokenIdentifier: String): EsdtTokenBalance

    // Get all issued ESDT/Fungible/Sft/Nft tokens
    fun getAllTokenIssued(type: EsdtTokenType): List<String>

    // Get ESDT token properties
    fun getEsdtProperties(tokenIdentifier: String): EsdtProperties

    // Get special roles for a token
    fun getEsdtSpecialRoles(tokenIdentifier: String): EsdtSpecialRoles?

    // Get all roles for tokens of an address
    fun getAllRolesForTokens(address: Address): HashMap<String, List<String>>

    // Get tokens where an address has a given role
    fun getTokensWithRole(address: Address, role: EsdtSpecialRole): List<String>

}
