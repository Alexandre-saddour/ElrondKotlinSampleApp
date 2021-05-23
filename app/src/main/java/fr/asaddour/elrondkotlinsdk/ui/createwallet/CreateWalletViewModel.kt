package fr.asaddour.elrondkotlinsdk.ui.createwallet

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.asaddour.elrondkotlinsdk.domain.wallet.CreateWalletUsecase
import fr.asaddour.elrondkotlinsdk.domain.wallet.SaveWalletUsecase
import fr.asaddour.elrondkotlinsdk.utils.SingleLiveEvent
import fr.asaddour.elrondkotlinsdk.utils.ext.launch
import kotlinx.coroutines.Dispatchers

class CreateWalletViewModel @ViewModelInject constructor(
    private val createWalletUsecase: CreateWalletUsecase,
    private val saveWalletUsecase: SaveWalletUsecase
) : ViewModel() {

    private val _viewAction = SingleLiveEvent<CreateWalletAction>()
    val viewAction: LiveData<CreateWalletAction> = _viewAction

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

    fun saveWallet(mnemonic: String) {
        launch(Dispatchers.IO) {
            val trimmedMnemonic = mnemonic.trim()
            if (trimmedMnemonic.split(" ").size != 24){
                _viewAction.postValue(CreateWalletAction.InvalidMnemonic)
            }
            else {
                saveWalletUsecase.execute(trimmedMnemonic)
                _viewAction.postValue(CreateWalletAction.CloseScreen)
            }
        }
    }

    sealed class CreateWalletViewState {
        data class GeneratedMnemonic(val mnemonic: String) : CreateWalletViewState()
    }

    sealed class CreateWalletAction {
        object InvalidMnemonic : CreateWalletAction()
        object CloseScreen : CreateWalletAction()
    }

}