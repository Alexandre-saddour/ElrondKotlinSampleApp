package fr.asaddour.elrondkotlinsdk.di

import com.elrond.erdkotlin.ErdSdk
import com.elrond.erdkotlin.domain.account.GetAccountUsecase
import com.elrond.erdkotlin.domain.networkconfig.GetNetworkConfigUsecase
import com.elrond.erdkotlin.domain.transaction.SendTransactionUsecase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object ErdUsecaseModule {

    // Make Elrond SDk classes injectable.

    @Provides
    fun provideGetAccountUsecase(): GetAccountUsecase = ErdSdk.getAccountUsecase()

    @Provides
    fun provideSendTransactionUsecase(): SendTransactionUsecase = ErdSdk.sendTransactionUsecase()

    @Provides
    fun provideGetNetworkConfigUsecase(): GetNetworkConfigUsecase = ErdSdk.getNetworkConfigUsecase()

}
