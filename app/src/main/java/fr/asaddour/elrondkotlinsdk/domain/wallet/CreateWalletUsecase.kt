package fr.asaddour.elrondkotlinsdk.domain.wallet

import com.elrond.erdkotlin.domain.wallet.Wallet
import javax.inject.Inject

class CreateWalletUsecase @Inject constructor() {

    fun execute() = Wallet.generateMnemonic()

}