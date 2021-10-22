package fr.asaddour.elrondkotlinsdk.domain.showcase

import android.util.Log
import com.elrond.erdkotlin.domain.esdt.GetAllIssuedEsdtUsecase
import com.elrond.erdkotlin.domain.esdt.GetAllRolesForTokensUsecase
import com.elrond.erdkotlin.domain.esdt.GetTokensWithRoleUsecase
import com.elrond.erdkotlin.domain.esdt.models.EsdtSpecialRole
import com.elrond.erdkotlin.domain.esdt.models.EsdtTokenType
import com.elrond.erdkotlin.domain.nft.GetNftDataUsecase
import com.elrond.erdkotlin.domain.nft.GetNftsRegisteredUsecase
import com.elrond.erdkotlin.domain.wallet.models.Address
import javax.inject.Inject

class ShowcaseNftApiUsecase @Inject constructor(
    private val getAllIssuedEsdtUsecase: GetAllIssuedEsdtUsecase,
    private val getAllRolesForTokensUsecase: GetAllRolesForTokensUsecase,
    private val getTokensWithRoleUsecase: GetTokensWithRoleUsecase,
    private val getNftDataUsecase: GetNftDataUsecase,
    private val getNftsRegisteredUsecase: GetNftsRegisteredUsecase
) {

    fun execute(address: Address) {
        getAllIssuedEsdtUsecase.execute(EsdtTokenType.ESDT).also {
            Log.d(TAG, "getAllIssuedEsdtUsecase: ESDT:$it")
        }
        getAllIssuedEsdtUsecase.execute(EsdtTokenType.Fungible).also {
            Log.d(TAG, "getAllIssuedEsdtUsecase: Fungible:$it")
        }
        getAllIssuedEsdtUsecase.execute(EsdtTokenType.NFT).also {
            Log.d(TAG, "getAllIssuedEsdtUsecase: NFT:$it")
        }
        getAllIssuedEsdtUsecase.execute(EsdtTokenType.SFT).also {
            Log.d(TAG, "getAllIssuedEsdtUsecase: SFT:$it")
        }
        getAllRolesForTokensUsecase.execute(address).also {
            Log.d(TAG, "getAllRolesForTokensUsecase: $it")
        }
        getTokensWithRoleUsecase.execute(address, EsdtSpecialRole.ESDTRoleLocalBurn).also {
            Log.d(TAG, "getTokensWithRoleUsecase ESDTRoleLocalBurn: $it")
        }
        getTokensWithRoleUsecase.execute(address, EsdtSpecialRole.ESDTRoleNFTCreate).also {
            Log.d(TAG, "getTokensWithRoleUsecase ESDTRoleNFTCreate: $it")
        }

        val tokenId = "EKT3433-866322"
        getNftDataUsecase.execute(
            address = address,
            tokenIdentifier = tokenId,
            nonce = 0
        ).also {
            Log.d(TAG, "getNftDataUsecase nonce=0: $it")
        }
        getNftDataUsecase.execute(
            address = address,
            tokenIdentifier = tokenId,
            nonce = 2
        ).also {
            Log.d(TAG, "getNftDataUsecase nonce=2: $it")
        }
        getNftsRegisteredUsecase.execute(address).also {
            Log.d(TAG, "getNftsRegisteredUsecase: $it")
        }
    }

    companion object {
        private const val TAG = "ShowcaseNftApiUsecase"
    }

}