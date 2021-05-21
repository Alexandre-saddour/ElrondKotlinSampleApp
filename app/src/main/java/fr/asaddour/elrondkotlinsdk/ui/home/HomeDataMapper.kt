package fr.asaddour.elrondkotlinsdk.ui.home

import com.elrond.erdkotlin.domain.account.models.Account

fun Account.toUi() = HomeViewModel.AccountUi(
    address = address.bech32(),
    balance = balance.toString(),
    nonce = nonce.toString(),
)
