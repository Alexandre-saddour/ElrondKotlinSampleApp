package fr.asaddour.elrondkotlinsdk.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.asaddour.elrondkotlinsdk.data.WalletRepositorySharedPref
import fr.asaddour.elrondkotlinsdk.domain.wallet.WalletRepository

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideLocationRepository(@ApplicationContext appContext: Context): WalletRepository =
        WalletRepositorySharedPref(appContext)

}
