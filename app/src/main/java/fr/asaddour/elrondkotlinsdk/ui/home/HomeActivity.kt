package fr.asaddour.elrondkotlinsdk.ui.home

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import fr.asaddour.elrondkotlinsdk.R
import fr.asaddour.elrondkotlinsdk.databinding.ActivityMainBinding
import fr.asaddour.elrondkotlinsdk.ui.createwallet.CreateWalletActivity


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        viewModel.viewState.observe(this) { viewState ->
            Log.d("HomeViewModel", "viewState:$viewState")
            when (viewState) {
                is HomeViewModel.HomeViewState.Content -> {
                    updateContent(viewState)
                }
                HomeViewModel.HomeViewState.Loading -> {
                    binding.loadingGroup.visibility = View.VISIBLE
                    binding.contentGroup.visibility = View.GONE
                }
            }
        }

        viewModel.viewAction.observe(this) { viewAction ->
            when (viewAction) {
                HomeViewModel.HomeAction.OpenCreateWalletScreen -> startActivity(
                    Intent(
                        this,
                        CreateWalletActivity::class.java
                    )
                )
                HomeViewModel.HomeAction.InvalidReceiverAddress -> binding.toAddressField.error =
                    getString(R.string.invalidAddress)
            }
        }
    }

    private fun initView() {
        binding.walletAddress.setOnLongClickListener { view ->
            (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).apply {
                val clip = ClipData.newPlainText("wallet address", (view as TextView).text)
                setPrimaryClip(clip)
            }
            Snackbar.make(view, "Address copied to clipboard", Snackbar.LENGTH_SHORT).show()
            true
        }
        binding.closeWalletButton.setOnClickListener {
            viewModel.deleteWallet()
        }
        binding.sendTransactionButton.setOnClickListener {
            viewModel.sendTransaction(
                toAddress = binding.toAddressField.text.toString(),
                amount = binding.transactionAmountField.text.toString(),
                message = binding.transactionMessageField.text.toString()
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateContent(viewState: HomeViewModel.HomeViewState.Content) {
        TransitionManager.beginDelayedTransition(
            binding.walletAddress.parent as ViewGroup,
            Slide(Gravity.BOTTOM)
        )
        binding.loadingGroup.visibility = View.GONE
        binding.contentGroup.visibility = View.VISIBLE
        binding.walletAddress.setText(viewState.account.address)
        binding.walletBalance.setText(viewState.account.balance)
        binding.walletNonce.setText(viewState.account.nonce)


        binding.sentTransactionTxField.apply {
            if (viewState.sentTransaction != null) {
                text = "${getString(R.string.tx)}: ${viewState.sentTransaction.txHash}\n" +
                        "${getString(R.string.status)}: ${viewState.sentTransaction.status}"
                setOnClickListener {
                    viewModel.fetchTransactionStatus(viewState.sentTransaction.txHash)
                }
            } else {
                text = ""
                setOnClickListener(null)
            }
        }


    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshAccountData()
    }
}