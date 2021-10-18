package com.elrond.erdkotlin.nft

import com.elrond.erdkotlin.domain.esdt.models.ManagementProperty
import com.elrond.erdkotlin.domain.esdt.utils.ValidateTokenNameAndTickerUsecase
import com.elrond.erdkotlin.domain.nft.IssueNftUsecase
import com.elrond.erdkotlin.domain.transaction.SendTransactionUsecase
import com.elrond.erdkotlin.domain.transaction.SignTransactionUsecase
import com.elrond.erdkotlin.helper.TestDataProvider
import com.elrond.erdkotlin.helper.TestUsecaseProvider
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mockito
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class IssueNftUsecaseTest {

    @Spy
    private val validateTokenNameAndTickerUsecase = ValidateTokenNameAndTickerUsecase()

    @Spy
    private val sendTransactionUsecase = SendTransactionUsecase(
        SignTransactionUsecase(),
        TestUsecaseProvider.transactionRepository
    )

    @InjectMocks
    lateinit var issueNftUsecase: IssueNftUsecase

    @Test
    fun `IssueNftUsecase should check name and ticker`() {

        val tokenName = "abcefg"
        val tokenTicker = "EKT"
        issueNftUsecase.execute(
            account = TestDataProvider.account,
            wallet = TestDataProvider.wallet,
            networkConfig = TestDataProvider.networkConfig,
            gasPrice = TestDataProvider.networkConfig.minGasPrice,
            tokenName = tokenName,
            tokenTicker = tokenTicker,
            type = IssueNftUsecase.Type.NFT
        )

        verify(validateTokenNameAndTickerUsecase, Mockito.times(1)).execute(
            tokenName,
            tokenTicker,
        )
    }

    @Test
    fun `Nft data should be well encoded`() {

        val tokenName = "abcefg"
        val tokenTicker = "EKT"
        val transaction = issueNftUsecase.execute(
            account = TestDataProvider.account,
            wallet = TestDataProvider.wallet,
            networkConfig = TestDataProvider.networkConfig,
            gasPrice = TestDataProvider.networkConfig.minGasPrice,
            tokenName = tokenName,
            tokenTicker = tokenTicker,
            type = IssueNftUsecase.Type.NFT
        )

        assertEquals(
            "issueNonFungible@616263656667@454b54",
            transaction.data
        )
    }

    @Test
    fun `Sft data should be well encoded`() {

        val tokenName = "abcefg"
        val tokenTicker = "EKT"
        val transaction = issueNftUsecase.execute(
            account = TestDataProvider.account,
            wallet = TestDataProvider.wallet,
            networkConfig = TestDataProvider.networkConfig,
            gasPrice = TestDataProvider.networkConfig.minGasPrice,
            tokenName = tokenName,
            tokenTicker = tokenTicker,
            type = IssueNftUsecase.Type.SFT
        )

        assertEquals(
            "issueSemiFungible@616263656667@454b54",
            transaction.data
        )
    }

    @Test
    fun `data with management rights should be well encoded`() {

        val tokenName = "abcefg"
        val tokenTicker = "EKT"
        val transaction = issueNftUsecase.execute(
            account = TestDataProvider.account,
            wallet = TestDataProvider.wallet,
            networkConfig = TestDataProvider.networkConfig,
            gasPrice = TestDataProvider.networkConfig.minGasPrice,
            tokenName = tokenName,
            tokenTicker = tokenTicker,
            type = IssueNftUsecase.Type.NFT,
            managementProperties = mapOf(
                ManagementProperty.CanPause to true,
                ManagementProperty.CanFreeze to true,
                ManagementProperty.CanWipe to true,
                ManagementProperty.CanTransferNFTCreateRole to true
            )
        )

        assertEquals(
            "issueNonFungible@616263656667@454b54@63616e5061757365@74727565@63616e467265657a65@74727565@63616e57697065@74727565@63616e5472616e736665724e4654437265617465526f6c65@74727565",
            transaction.data
        )
    }

    @Test
    fun `should not set unsupported managementProperties`() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            val tokenName = "abcefg"
            val tokenTicker = "EKT"
            issueNftUsecase.execute(
                account = TestDataProvider.account,
                wallet = TestDataProvider.wallet,
                networkConfig = TestDataProvider.networkConfig,
                gasPrice = TestDataProvider.networkConfig.minGasPrice,
                tokenName = tokenName,
                tokenTicker = tokenTicker,
                type = IssueNftUsecase.Type.NFT,
                managementProperties = mapOf(
                    ManagementProperty.CanBurn to true,
                    ManagementProperty.CanMint to true,
                    ManagementProperty.CanAddSpecialRoles to true,
                    ManagementProperty.CanChangeOwner to true,
                    ManagementProperty.CanUpgrade to true
                )
            )
        }
    }

}