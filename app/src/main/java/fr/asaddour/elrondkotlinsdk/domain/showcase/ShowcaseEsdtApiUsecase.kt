package fr.asaddour.elrondkotlinsdk.domain.showcase

import android.util.Log
import com.elrond.erdkotlin.domain.esdt.*
import com.elrond.erdkotlin.domain.esdt.models.EsdtTokenType
import com.elrond.erdkotlin.domain.wallet.models.Address
import javax.inject.Inject

class ShowcaseEsdtApiUsecase @Inject constructor(
    private val getAllEsdtUsecase: GetAllEsdtUsecase,
    private val getAllIssuedEsdtUsecase: GetAllIssuedEsdtUsecase,
    private val getEsdtBalanceUsecase: GetEsdtBalanceUsecase,
    private val getEsdtPropertiesUsecase: GetEsdtPropertiesUsecase,
    private val getEsdtSpecialRolesUsecase: GetEsdtSpecialRolesUsecase
) {

    fun execute(address: Address) {
        val allEsdtIssued = getAllIssuedEsdtUsecase.execute(EsdtTokenType.ESDT)
        Log.d(TAG, "There are ${allEsdtIssued.size} ESDT tokens issued on the network")

        for (esdt in allEsdtIssued) {
            val esdtSpecialRoles = getEsdtSpecialRolesUsecase.execute(esdt)
            if (esdtSpecialRoles != null) {
                val esdtProperties = getEsdtPropertiesUsecase.execute(esdt)
                Log.d(TAG, "Properties of $esdt: $esdtProperties")
                Log.d(TAG, "SpecialRoles of $esdt: $esdtSpecialRoles")
                break
            }
        }

        val esdtTokens = try {
            getAllEsdtUsecase.execute(address)
                .toList()
                .takeLast(3)
                .map { (_, esdt) ->
                    getEsdtBalanceUsecase.execute(address, esdt.tokenIdentifier)
                }
        } catch (e: Exception) {
            emptyList()
        }

        Log.d(TAG, "Address: ${address.bech32} has ${esdtTokens.size} esdts, including:")
        esdtTokens.forEach { esdtToken ->
            Log.d(TAG, "\t- $esdtToken")
        }

    }

    companion object {
        const val TAG = "ShowcaseEsdtUsecase"
    }

}