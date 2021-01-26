package fr.asaddour.elrondkotlinsdk.domain.wallet

import com.elrond.erdkotlin.domain.wallet.Wallet
import javax.inject.Inject

class ImportWalletUsecase @Inject constructor(
    private val repository: WalletRepository
) {

    fun execute(mnemonic: String) {
        // import wallet
        val wallet = Wallet.createFromMnemonic(mnemonic, 0)
        // save its private key
        repository.setPrivateKey(wallet.privateKeyHex)
    }

}