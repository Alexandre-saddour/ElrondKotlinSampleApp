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
import fr.asaddour.elrondkotlinsdk.ui.createwallet.CreateWalletActivity
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        viewModel.viewState.observe(this) { viewState ->
            Log.d("HomeViewModel", "viewState:$viewState")
            when (viewState) {
                is HomeViewModel.HomeViewState.Content -> {
                    updateContent(viewState)
                }
                HomeViewModel.HomeViewState.OpenCreateWalletScreen -> startActivity(
                    Intent(
                        this,
                        CreateWalletActivity::class.java
                    )
                )
                HomeViewModel.HomeViewState.Loading -> {
                    loadingGroup.visibility = View.VISIBLE
                    contentGroup.visibility = View.GONE
                }
                HomeViewModel.HomeViewState.InvalidReceiverAddress -> toAddressField.error =
                    getString(R.string.invalidAddress)
            }
        }
    }

    private fun initView() {
        walletAddress.setOnLongClickListener { view ->
            (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).apply {
                val clip = ClipData.newPlainText("wallet address", (view as TextView).text)
                setPrimaryClip(clip)
            }
            Snackbar.make(view, "Address copied to clipboard", Snackbar.LENGTH_SHORT).show()
            true
        }
        closeWalletButton.setOnClickListener {
            viewModel.deleteWallet()
        }
        sendTransactionButton.setOnClickListener {
            viewModel.sendTransaction(
                toAddress = toAddressField.text.toString(),
                amount = transactionAmountField.text.toString(),
                message = transactionMessageField.text.toString()
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateContent(viewState: HomeViewModel.HomeViewState.Content) {
        TransitionManager.beginDelayedTransition(
            walletAddress.parent as ViewGroup,
            Slide(Gravity.BOTTOM)
        )
        loadingGroup.visibility = View.GONE
        contentGroup.visibility = View.VISIBLE
        walletAddress.setText(viewState.account.address)
        walletBalance.setText(viewState.account.balance)
        walletNonce.setText(viewState.account.nonce)


        sentTransactionTxField.apply {
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