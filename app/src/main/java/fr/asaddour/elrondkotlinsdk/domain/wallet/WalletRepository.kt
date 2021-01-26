package fr.asaddour.elrondkotlinsdk.domain.wallet

interface WalletRepository {

    fun getPrivateKey(): String?
    fun setPrivateKey(privateKey: String?)
}