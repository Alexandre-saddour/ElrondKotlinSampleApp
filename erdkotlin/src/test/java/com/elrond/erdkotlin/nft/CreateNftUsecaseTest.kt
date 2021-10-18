package com.elrond.erdkotlin.nft

import com.elrond.erdkotlin.domain.nft.CreateNftUsecase
import com.elrond.erdkotlin.domain.transaction.SendTransactionUsecase
import com.elrond.erdkotlin.domain.transaction.SignTransactionUsecase
import com.elrond.erdkotlin.helper.TestDataProvider
import com.elrond.erdkotlin.helper.TestUsecaseProvider
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CreateNftUsecaseTest {

    @Spy
    private val sendTransactionUsecase = SendTransactionUsecase(
        SignTransactionUsecase(),
        TestUsecaseProvider.transactionRepository
    )

    @InjectMocks
    lateinit var createNftUsecase: CreateNftUsecase

    @Test
    fun `uris must not be empty`() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            createNftUsecase.execute(
                account = TestDataProvider.account,
                wallet = TestDataProvider.wallet,
                networkConfig = TestDataProvider.networkConfig,
                gasPrice = TestDataProvider.networkConfig.minGasPrice,
                tokenIdentifier = "tokenIdentifier",
                initialQuantity = 1.toBigInteger(),
                nftName = "ErdKotlinNft",
                royalties = 0,
                uris = emptyList(),
                hash = "kotlin sdk for Elrond",
                attributes = "ErdKotlin attributes"
            )
        }
    }

    @Test
    fun `royalities must not be higher than 10_000`() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            createNftUsecase.execute(
                account = TestDataProvider.account,
                wallet = TestDataProvider.wallet,
                networkConfig = TestDataProvider.networkConfig,
                gasPrice = TestDataProvider.networkConfig.minGasPrice,
                tokenIdentifier = "tokenIdentifier",
                initialQuantity = 1.toBigInteger(),
                nftName = "ErdKotlinNft",
                royalties = 10001,
                uris = listOf("https://github.com/ElrondNetwork/elrond-sdk-erdkotlin"),
                hash = "kotlin sdk for Elrond",
                attributes = "ErdKotlin attributes"
            )
        }
    }

    @Test
    fun `royalities must be positive`() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            createNftUsecase.execute(
                account = TestDataProvider.account,
                wallet = TestDataProvider.wallet,
                networkConfig = TestDataProvider.networkConfig,
                gasPrice = TestDataProvider.networkConfig.minGasPrice,
                tokenIdentifier = "tokenIdentifier",
                initialQuantity = 1.toBigInteger(),
                nftName = "ErdKotlinNft",
                royalties = -1,
                uris = listOf("https://github.com/ElrondNetwork/elrond-sdk-erdkotlin"),
                hash = "kotlin sdk for Elrond",
                attributes = "ErdKotlin attributes"
            )
        }
    }

    @Test
    fun `data must be well encoded`() {
        val transaction = createNftUsecase.execute(
            account = TestDataProvider.account,
            wallet = TestDataProvider.wallet,
            networkConfig = TestDataProvider.networkConfig,
            gasPrice = TestDataProvider.networkConfig.minGasPrice,
            tokenIdentifier = "tokenIdentifier",
            initialQuantity = 1.toBigInteger(),
            nftName = "ErdKotlinNft",
            royalties = 123,
            uris = listOf("https://github.com/ElrondNetwork/elrond-sdk-erdkotlin"),
            hash = "kotlin sdk for Elrond",
            attributes = "ErdKotlin attributes"
        )

        assertEquals(
            "ESDTNFTCreate@746f6b656e4964656e746966696572@01@4572644b6f746c696e4e6674@7B@6b6f746c696e2073646b20666f7220456c726f6e64@4572644b6f746c696e2061747472696275746573@68747470733a2f2f6769746875622e636f6d2f456c726f6e644e6574776f726b2f656c726f6e642d73646b2d6572646b6f746c696e",
            transaction.data
        )
    }
}