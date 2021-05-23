package fr.asaddour.elrondkotlinsdk.ui.createwallet

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import fr.asaddour.elrondkotlinsdk.R
import fr.asaddour.elrondkotlinsdk.databinding.ActivityCreateWalletBinding
import fr.asaddour.elrondkotlinsdk.ui.home.HomeActivity

@AndroidEntryPoint
class CreateWalletActivity : AppCompatActivity() {

    private val viewModel by viewModels<CreateWalletViewModel>()
    private lateinit var binding: ActivityCreateWalletBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        initView()
        viewModel.viewState.observe(this) { viewState ->
            when (viewState) {
                is CreateWalletViewModel.CreateWalletViewState.GeneratedMnemonic -> updateView(
                    viewState
                )
            }
        }
        viewModel.viewAction.observe(this) { viewAction ->
            when (viewAction){
                CreateWalletViewModel.CreateWalletAction.CloseScreen -> startActivity(Intent(this, HomeActivity::class.java))
                CreateWalletViewModel.CreateWalletAction.InvalidMnemonic -> showInvalidMnemonic()
            }
        }
    }

    private fun initView() {
        binding.generateMnemonicButton.setOnClickListener {
            viewModel.createWallet()
        }
        binding.importWalletButton.setOnClickListener {
            viewModel.saveWallet(binding.mnemonicField.text.toString())
        }
    }

    private fun updateView(viewState: CreateWalletViewModel.CreateWalletViewState.GeneratedMnemonic) {
        binding.mnemonicField.setText(viewState.mnemonic)
        binding.mnemonicField.error = null
    }

    private fun showInvalidMnemonic() {
        binding.mnemonicField.error = getString(R.string.enter_24_words)
    }

}