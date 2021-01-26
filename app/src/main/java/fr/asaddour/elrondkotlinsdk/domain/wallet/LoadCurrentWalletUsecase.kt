package fr.asaddour.elrondkotlinsdk.domain.wallet

import com.elrond.erdkotlin.domain.wallet.Wallet
import javax.inject.Inject

class LoadCurrentWalletUsecase @Inject constructor(
    private val walletRepository: WalletRepository
) {
    fun execute(): Wallet? {
        val privateKey = walletRepository.getPrivateKey() ?: return null
        return Wallet.createFromPrivateKey(privateKey)
    }

}
