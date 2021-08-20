package fr.asaddour.elrondkotlinsdk.di

import com.elrond.erdkotlin.ErdSdk
import com.elrond.erdkotlin.domain.account.GetAccountUsecase
import com.elrond.erdkotlin.domain.account.GetAddressBalanceUsecase
import com.elrond.erdkotlin.domain.account.GetAddressNonceUsecase
import com.elrond.erdkotlin.domain.dns.RegisterDnsUsecase
import com.elrond.erdkotlin.domain.esdt.*
import com.elrond.erdkotlin.domain.esdt.management.*
import com.elrond.erdkotlin.domain.networkconfig.GetNetworkConfigUsecase
import com.elrond.erdkotlin.domain.transaction.*
import com.elrond.erdkotlin.domain.sc.CallContractUsecase
import com.elrond.erdkotlin.domain.vm.query.QueryContractUsecase
import com.elrond.erdkotlin.domain.vm.query.hex.QueryContractHexUsecase
import com.elrond.erdkotlin.domain.vm.query.integer.QueryContractIntUsecase
import com.elrond.erdkotlin.domain.vm.query.string.QueryContractStringUsecase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
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
    fun provideQueryContractUsecase(): QueryContractUsecase = ErdSdk.queryContractUsecase()

    @Provides
    fun provideQueryContractHexUsecase(): QueryContractHexUsecase = ErdSdk.queryContractHexUsecase()

    @Provides
    fun provideQueryContractStringUsecase(): QueryContractStringUsecase = ErdSdk.queryContractStringUsecase()

    @Provides
    fun provideQueryContractIntUsecase(): QueryContractIntUsecase = ErdSdk.queryContracInttUsecase()

    @Provides
    fun provideCallContractUsecase(): CallContractUsecase = ErdSdk.callContractUsecase()

    @Provides
    fun provideGetAllEsdtUsecase(): GetAllEsdtUsecase = ErdSdk.getAllEsdtUsecase()

    @Provides
    fun provideGetAllIssuedEsdtUsecase(): GetAllIssuedEsdtUsecase = ErdSdk.getAllIssuedEsdtUsecase()

    @Provides
    fun provideGetEsdtBalanceUsecase(): GetEsdtBalanceUsecase = ErdSdk.getEsdtBalanceUsecase()

    @Provides
    fun provideGetEsdtPropertiesUsecase(): GetEsdtPropertiesUsecase = ErdSdk.getEsdtPropertiesUsecase()

    @Provides
    fun provideGetEsdtSpecialRolesUsecase(): GetEsdtSpecialRolesUsecase = ErdSdk.getEsdtSpecialRolesUsecase()

    @Provides
    fun provideIssueEsdtUsecase(): IssueEsdtUsecase = ErdSdk.getIssueEsdtUsecase()

    @Provides
    fun provideBurnEsdtUsecase(): BurnEsdtUsecase = ErdSdk.getBurnEsdtUsecase()

    @Provides
    fun provideChangeOwnerEsdtUsecase(): ChangeOwnerEsdtUsecase = ErdSdk.getChangeOwnerEsdtUsecase()

    @Provides
    fun provideFreezeAccountEsdtUsecase(): FreezeAccountEsdtUsecase = ErdSdk.getFreezeAccountEsdtUsecase()

    @Provides
    fun provideMintEsdtUsecase(): MintEsdtUsecase = ErdSdk.getMintEsdtUsecase()

    @Provides
    fun providePauseAccountEsdtUsecase(): PauseAccountEsdtUsecase = ErdSdk.getPauseAccountEsdtUsecase()

    @Provides
    fun provideSetSpecialRolesToAccountEsdtUsecase(): SetSpecialRolesEsdtUsecase = ErdSdk.getSetSpecialRolesToAccountEsdtUsecase()

    @Provides
    fun provideTransferEsdtUsecase(): TransferEsdtUsecase = ErdSdk.getTransferEsdtUsecase()

    @Provides
    fun provideUpgradeEsdtUsecase(): UpgradeEsdtUsecase = ErdSdk.getUpgradeEsdtUsecase()

    @Provides
    fun provideWipeAccountEsdtUsecase(): WipeAccountEsdtUsecase = ErdSdk.getWipeAccountEsdtUsecase()

}
