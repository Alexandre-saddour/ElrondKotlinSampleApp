package fr.asaddour.elrondkotlinsdk.ui.createwallet

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.asaddour.elrondkotlinsdk.domain.wallet.CreateWalletUsecase
import fr.asaddour.elrondkotlinsdk.domain.wallet.ImportWalletUsecase
import fr.asaddour.elrondkotlinsdk.extentions.launch
import kotlinx.coroutines.Dispatchers

class CreateWalletViewModel @ViewModelInject constructor(
    private val createWalletUsecase: CreateWalletUsecase,
    private val importWalletUsecase: ImportWalletUsecase
) : ViewModel() {

    private val _viewState = MutableLiveData<CreateWalletViewState>()
    val viewState: LiveData<CreateWalletViewState> = _viewState

    fun createWallet() {
        launch(Dispatchers.IO) {
            val mnemonic = createWalletUsecase.execute()
            _viewState.postValue(
                CreateWalletViewState.GeneratedMnemonic(mnemonic.joinToString())
            )
        }
    }

    fun importWallet(mnemonic: String) {
        launch(Dispatchers.IO) {
            val trimmedMnemonic = mnemonic.trim()
            if (trimmedMnemonic.split(" ").size != 24){
                _viewState.postValue(CreateWalletViewState.InvalidMnemonic)
            }
            else {
                importWalletUsecase.execute(trimmedMnemonic)
                _viewState.postValue(CreateWalletViewState.CloseScreen)
            }

        }
    }

    sealed class CreateWalletViewState {
        data class GeneratedMnemonic(val mnemonic: String) : CreateWalletViewState()
        object InvalidMnemonic : CreateWalletViewState()
        object CloseScreen : CreateWalletViewState()
    }

}