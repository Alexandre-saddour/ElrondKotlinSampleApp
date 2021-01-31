package com.elrond.erdkotlin

import com.elrond.erdkotlin.TestHelper.alicePrivateKey
import com.elrond.erdkotlin.TestHelper.transactionWithData
import com.elrond.erdkotlin.domain.wallet.models.Wallet
import com.elrond.erdkotlin.domain.transaction.SignTransactionUsecase
import com.elrond.erdkotlin.domain.transaction.models.Transaction
import com.elrond.erdkotlin.domain.wallet.models.Address
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test

class SignTransactionUsecaseTest {

    private val wallet = Wallet.createFromPrivateKey(alicePrivateKey)

    @Test
    @Throws(Exception::class)
    fun signWithDataField() {
        // With data field
        val transaction = transactionWithData()
        val expectedSignature =
            "096c571889352947f285632d79f2b2ee1b81e7acd19ee20510d34002eba0f999b4720f50211b039dd40914284f84c14eb84815bb045c14dbed036f2e87431307"
        val expectedJson =
            "{'nonce':7,'value':'10000000000000000000','receiver':'erd1cux02zersde0l7hhklzhywcxk4u9n4py5tdxyx7vrvhnza2r4gmq4vw35r','sender':'erd1l453hd0gt5gzdp7czpuall8ggt2dcv5zwmfdf3sd3lguxseux2fsmsgldz','gasPrice':1000000000,'gasLimit':70000,'data':'Zm9yIHRoZSBib29rIHdpdGggc3Rha2U=','chainID':'1','version':1,'signature':'$expectedSignature'}".replace(
                '\'',
                '"'
            )

        val signedTransaction = SignTransactionUsecase().execute(transaction, wallet)

        Assert.assertEquals(expectedSignature, signedTransaction.signature)
        Assert.assertEquals(expectedJson, signedTransaction.serialize())
    }

    @Test
    @Ignore
    @Throws(Exception::class)
    fun signWithUsername() {
        // With data field
        val transaction = Transaction(
            sender = Address.fromBech32("erd1qyu5wthldzr8wx5c9ucg8kjagg0jfs53s8nr3zpz3hypefsdd8ssycr6th"),
            receiver = Address.fromBech32("erd1spyavw0956vq68xj8y4tenjpq2wd5a9p2c6j8gsz7ztyrnpxrruqzu66jx"),
            value = 0.toBigInteger(),
            senderUsername = "alice",
            receiverUsername = "bob",
            data = "",
            chainID = "local-testnet",
            gasPrice = 1000000000,
            gasLimit = 50000,
            nonce = 89
        )
        val expectedSerialized =
            """{"nonce":89,"value":"0","receiver":"erd1spyavw0956vq68xj8y4tenjpq2wd5a9p2c6j8gsz7ztyrnpxrruqzu66jx","sender":"erd1qyu5wthldzr8wx5c9ucg8kjagg0jfs53s8nr3zpz3hypefsdd8ssycr6th","senderUsername":"YWxpY2U=","receiverUsername":"Ym9i","gasPrice":1000000000,"gasLimit":50000,"chainID":"local-testnet","version":1}"""

        val expectedSignature =
            "1bed82c3f91c9d24f3a163e7b93a47453d70e8743201fe7d3656c0214569566a76503ef0968279ac942ca43b9c930bd26638dfb075a220ce80b058ab7bca140a"
        val expectedJson =
            "{'nonce':7,'value':'10000000000000000000','receiver':'erd1cux02zersde0l7hhklzhywcxk4u9n4py5tdxyx7vrvhnza2r4gmq4vw35r','sender':'erd1l453hd0gt5gzdp7czpuall8ggt2dcv5zwmfdf3sd3lguxseux2fsmsgldz','gasPrice':1000000000,'gasLimit':70000,'data':'Zm9yIHRoZSBib29rIHdpdGggc3Rha2U=','chainID':'1','version':1,'signature':'$expectedSignature'}".replace(
                '\'',
                '"'
            )

        val signedTransaction = SignTransactionUsecase().execute(transaction, wallet)

        Assert.assertEquals(expectedSerialized, transaction.serialize())
        Assert.assertEquals(expectedSignature, signedTransaction.signature)
//        Assert.assertEquals(expectedJson, signedTransaction.serialize())
    }


    @Test
    fun signWithoutDataField() {
        // Without data field
        val transaction = TestHelper.transactionWithoutData()
        val expectedSignature =
            "4a6d8186eae110894e7417af82c9bf9592696c0600faf110972e0e5310d8485efc656b867a2336acec2b4c1e5f76c9cc70ba1803c6a46455ed7f1e2989a90105"
        val expectedJson =
            "{'nonce':8,'value':'10000000000000000000','receiver':'erd1cux02zersde0l7hhklzhywcxk4u9n4py5tdxyx7vrvhnza2r4gmq4vw35r','sender':'erd1l453hd0gt5gzdp7czpuall8ggt2dcv5zwmfdf3sd3lguxseux2fsmsgldz','gasPrice':1000000000,'gasLimit':50000,'chainID':'1','version':1,'signature':'$expectedSignature'}".replace(
                '\'',
                '"'
            )
        val signedTransaction = SignTransactionUsecase().execute(transaction, wallet)

        Assert.assertEquals(expectedSignature, signedTransaction.signature)
        Assert.assertEquals(expectedJson, signedTransaction.serialize())
    }

//    @Test
//    fun signWithoutDataField2() {
//        // Without data field
//        val transaction = Transaction(
//            nonce = 33,
//            value = "0".toBigInteger(),
//            sender = Address.fromBech32("erd1aj4uqfzkuusznlar78d4yp2d3fpwd9zz2tdeghhquqpv9fs88tzqws5xtk"),
//            receiver = Address.fromBech32("erd1sytay9nan2fnjx069tesszrh5lxns940w3pgay8qltqrwcsquq9s44ee83"),
//            gasPrice = 1000000000,
//            gasLimit = 14000000,
//            chainID = "D"
//        )
//        val expectedSignature =
//            "c5dcc34986734936f013412e60f87e7c77ee064b4ddf86a502b7c1ec97776939ec7b2ca97825f89f4c483e502b4b0264b50f55dcf9445467ec2df9f78cfd1d02"
//        val signedTransaction = SignTransactionUsecase().execute(transaction, wallet)
//
//        Assert.assertEquals(expectedSignature, signedTransaction.signature)
//    }


}