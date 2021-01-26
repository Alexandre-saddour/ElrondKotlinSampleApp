package fr.asaddour.elrondkotlinsdk.ui.createwallet

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import fr.asaddour.elrondkotlinsdk.R
import fr.asaddour.elrondkotlinsdk.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_create_wallet.*

@AndroidEntryPoint
class CreateWalletActivity : AppCompatActivity() {

    private val viewModel by viewModels<CreateWalletViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_wallet)
        setSupportActionBar(findViewById(R.id.toolbar))
        initView()
        viewModel.viewState.observe(this) { viewState ->
            when (viewState) {
                is CreateWalletViewModel.CreateWalletViewState.GeneratedMnemonic -> updateView(
                    viewState
                )
                CreateWalletViewModel.CreateWalletViewState.CloseScreen -> startActivity(Intent(this, HomeActivity::class.java))
                CreateWalletViewModel.CreateWalletViewState.InvalidMnemonic -> mnemonicField.error =
                    getString(R.string.enter_24_words)
            }
        }
    }

    private fun initView() {
        generateMnemonicButton.setOnClickListener {
            viewModel.createWallet()
        }
        importWalletButton.setOnClickListener {
            viewModel.importWallet(mnemonicField.text.toString())
        }
    }

    private fun updateView(viewState: CreateWalletViewModel.CreateWalletViewState.GeneratedMnemonic) {
        mnemonicField.setText(viewState.mnemonic)
        mnemonicField.error = null
    }

}