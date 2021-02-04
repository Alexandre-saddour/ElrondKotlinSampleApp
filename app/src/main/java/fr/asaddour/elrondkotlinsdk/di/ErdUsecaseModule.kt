package fr.asaddour.elrondkotlinsdk.di

import com.elrond.erdkotlin.ErdSdk
import com.elrond.erdkotlin.domain.account.GetAccountUsecase
import com.elrond.erdkotlin.domain.account.GetAddressBalanceUsecase
import com.elrond.erdkotlin.domain.account.GetAddressNonceUsecase
import com.elrond.erdkotlin.domain.dns.RegisterDnsUsecase
import com.elrond.erdkotlin.domain.networkconfig.GetNetworkConfigUsecase
import com.elrond.erdkotlin.domain.transaction.*
import com.elrond.erdkotlin.domain.vm.QuerySmartContractUsecase
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
    fun provideGetAddressNonceUsecase(): GetAddressNonceUsecase = ErdSdk.getAddressNonceUsecase()

    @Provides
    fun provideGetAddressBalanceUsecase(): GetAddressBalanceUsecase = ErdSdk.getAddressBalanceUsecase()

    @Provides
    fun provideGetNetworkConfigUsecase(): GetNetworkConfigUsecase = ErdSdk.getNetworkConfigUsecase()

    @Provides
    fun provideSendTransactionUsecase(): SendTransactionUsecase = ErdSdk.sendTransactionUsecase()

    @Provides
    fun provideGetAddressTransactionsUsecase(): GetAddressTransactionsUsecase = ErdSdk.getTransactionsUsecase()

    @Provides
    fun providegetTransactionInfoUsecase(): GetTransactionInfoUsecase = ErdSdk.getTransactionInfoUsecase()

    @Provides
    fun providegetTransactionStatusUsecase(): GetTransactionStatusUsecase = ErdSdk.getTransactionStatusUsecase()

    @Provides
    fun provideEstimateCostOfTransactionUsecase(): EstimateCostOfTransactionUsecase = ErdSdk.estimateCostOfTransactionUsecase()

    @Provides
    fun provideRegisterDnsUsecase(): RegisterDnsUsecase = ErdSdk.registerDnsUsecase()

    @Provides
    fun provideQuerySmartContractUsecase(): QuerySmartContractUsecase = ErdSdk.querySmartContractUsecase()

}
