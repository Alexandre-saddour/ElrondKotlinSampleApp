package fr.asaddour.elrondkotlinsdk.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import fr.asaddour.elrondkotlinsdk.SampleApp
import fr.asaddour.elrondkotlinsdk.data.WalletRepositorySharedPref
import fr.asaddour.elrondkotlinsdk.domain.wallet.WalletRepository

@Module
@InstallIn(ActivityComponent::class)
object RepositoryModule {

    @Provides
    fun provideLocationRepository(): WalletRepository = WalletRepositorySharedPref(
        SampleApp.applicationContext
    )

}
