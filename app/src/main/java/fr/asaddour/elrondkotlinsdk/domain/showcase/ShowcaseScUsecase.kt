package fr.asaddour.elrondkotlinsdk.domain.showcase

import android.util.Log
import com.elrond.erdkotlin.domain.account.GetAccountUsecase
import com.elrond.erdkotlin.domain.networkconfig.GetNetworkConfigUsecase
import com.elrond.erdkotlin.domain.sc.CallContractUsecase
import com.elrond.erdkotlin.domain.vm.query.QueryContractUsecase
import com.elrond.erdkotlin.domain.vm.query.hex.QueryContractHexUsecase
import com.elrond.erdkotlin.domain.vm.query.integer.QueryContractIntUsecase
import com.elrond.erdkotlin.domain.vm.query.string.QueryContractStringUsecase
import com.elrond.erdkotlin.domain.wallet.models.Address
import com.elrond.erdkotlin.domain.wallet.models.Wallet
import javax.inject.Inject

class ShowcaseScUsecase @Inject constructor(
    private val getAccountUsecase: GetAccountUsecase,
    private val getNetworkConfigUsecase: GetNetworkConfigUsecase,
    private val queryContractUsecase: QueryContractUsecase,
    private val queryContractHexUsecase: QueryContractHexUsecase,
    private val queryContractStringUsecase: QueryContractStringUsecase,
    private val queryContractIntUsecase: QueryContractIntUsecase,
    private val callContractUsecase: CallContractUsecase
) {

    fun execute(wallet: Wallet){

        try {
            executeArgContract(wallet)
        }
        catch (e: Exception){
            e.printStackTrace()
            // may receive an "account not found" exception.
        }

    }

    private fun executeArgContract(wallet: Wallet) {

        val queryOutput = queryContractUsecase.execute(
            contractAddress = Address.fromBech32("erd1qqqqqqqqqqqqqpgqjwdxetzzy8pjj76eqzwyqygtl5n9ruuct9usk5rrl0"),
            funcName = "echo_first_argument_view",
            args = listOf("FF", "5745474c442d616263646566", "DEADBEEF")
        )
        Log.d(TAG, "queryOutput: $queryOutput")

        val account = getAccountUsecase.execute(Address.fromHex(wallet.publicKeyHex))
        val networkConfig = getNetworkConfigUsecase.execute()
        val arg1 = callContractUsecase.execute(
            account = account,
            wallet = wallet,
            networkConfig = networkConfig,
            gasPrice = networkConfig.minGasPrice * 2,
            gasLimit = 200000000,
            contractAddress = Address.fromBech32("erd1qqqqqqqqqqqqqpgqjwdxetzzy8pjj76eqzwyqygtl5n9ruuct9usk5rrl0"),
            funcName = "echo_third_argument_view",
            args = listOf("120", "0x5745474c442d616263646566", "0xDEADBEEF")
        )
        Log.d(TAG, "arg1: $arg1")
    }


    private fun executeCounterContract(wallet: Wallet) {
        val account = getAccountUsecase.execute(Address.fromHex(wallet.publicKeyHex))
        val networkConfig = getNetworkConfigUsecase.execute()
        val incr = callContractUsecase.execute(
            account = account,
            wallet = wallet,
            networkConfig = networkConfig,
            gasPrice = networkConfig.minGasPrice * 2,
            gasLimit = networkConfig.minGasLimit * 2,
            contractAddress = Address.fromBech32("erd1qqqqqqqqqqqqqpgqagvtnqn9dgnx7a6stw4n92kufathjrfd8tzqf80mkz"),
            funcName = "increment",
        )
        val get = queryContractUsecase.execute(
            contractAddress = Address.fromBech32("erd1qqqqqqqqqqqqqpgqagvtnqn9dgnx7a6stw4n92kufathjrfd8tzqf80mkz"),
            funcName = "get",
        )
        val getHex = queryContractHexUsecase.execute(
            contractAddress = Address.fromBech32("erd1qqqqqqqqqqqqqpgqagvtnqn9dgnx7a6stw4n92kufathjrfd8tzqf80mkz"),
            funcName = "get",
        )
        val getString = queryContractStringUsecase.execute(
            contractAddress = Address.fromBech32("erd1qqqqqqqqqqqqqpgqagvtnqn9dgnx7a6stw4n92kufathjrfd8tzqf80mkz"),
            funcName = "get",
        )
        val getInt = queryContractIntUsecase.execute(
            contractAddress = Address.fromBech32("erd1qqqqqqqqqqqqqpgqagvtnqn9dgnx7a6stw4n92kufathjrfd8tzqf80mkz"),
            funcName = "get",
        )
        Log.d(TAG, "incr: $incr")
        Log.d(TAG, "get: $get")
        Log.d(TAG, "getHex: $getHex")
        Log.d(TAG, "getString: $getString")
        Log.d(TAG, "getInt: $getInt")
    }

    companion object {
        const val TAG = "ShowcaseScUsecase"
    }

}