package com.elrond.erdkotlin.nft

import com.elrond.erdkotlin.domain.nft.TransferNFTCreateRoleUsecase
import com.elrond.erdkotlin.domain.transaction.SendTransactionUsecase
import com.elrond.erdkotlin.domain.transaction.SignTransactionUsecase
import com.elrond.erdkotlin.domain.wallet.models.Address
import com.elrond.erdkotlin.helper.TestDataProvider
import com.elrond.erdkotlin.helper.TestUsecaseProvider
import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TransferNFTCreateRoleUsecaseTest {

    @Spy
    private val sendTransactionUsecase = SendTransactionUsecase(
        SignTransactionUsecase(),
        TestUsecaseProvider.transactionRepository
    )

    @InjectMocks
    lateinit var transferNFTCreateRoleUsecase: TransferNFTCreateRoleUsecase


    @Test
    fun `data should be well encoded`() {

        val transaction = transferNFTCreateRoleUsecase.execute(
            account = TestDataProvider.account,
            wallet = TestDataProvider.wallet,
            networkConfig = TestDataProvider.networkConfig,
            gasPrice = TestDataProvider.networkConfig.minGasPrice,
            tokenIdentifier = "tokenId",
            roleReceiver = Address.fromBech32("erd1l453hd0gt5gzdp7czpuall8ggt2dcv5zwmfdf3sd3lguxseux2fsmsgldz"),
        )

        Assert.assertEquals(
            "transferNFTCreateRole@746f6b656e4964@fd691bb5e85d102687d81079dffce842d4dc328276d2d4c60d8fd1c3433c3293@fd691bb5e85d102687d81079dffce842d4dc328276d2d4c60d8fd1c3433c3293",
            transaction.data
        )
    }

}