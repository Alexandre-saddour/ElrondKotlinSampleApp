package fr.asaddour.elrondkotlinsdk.data

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import fr.asaddour.elrondkotlinsdk.domain.wallet.WalletRepository


// This implementation is obviously un-secure but good enough for a sample app
class WalletRepositorySharedPref(context: Context) : WalletRepository {

    private val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    override fun getPrivateKey() = defaultSharedPreferences.getString(PRIVATE_KEY, null)
    override fun setPrivateKey(privateKey: String?) = defaultSharedPreferences.edit {
        putString(PRIVATE_KEY, privateKey)
    }

    companion object {
        const val PRIVATE_KEY = "private_key"
    }
}