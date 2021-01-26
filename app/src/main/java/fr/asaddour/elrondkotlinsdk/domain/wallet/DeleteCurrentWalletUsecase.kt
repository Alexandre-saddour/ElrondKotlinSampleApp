package fr.asaddour.elrondkotlinsdk.domain.wallet

import javax.inject.Inject

class DeleteCurrentWalletUsecase @Inject constructor(
    private val walletRepository: WalletRepository
) {
    fun execute() {
        walletRepository.setPrivateKey(null)
    }
}
