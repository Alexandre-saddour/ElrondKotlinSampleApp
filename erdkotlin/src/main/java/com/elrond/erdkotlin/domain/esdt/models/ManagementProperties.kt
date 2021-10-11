package com.elrond.erdkotlin.domain.esdt.models

enum class ManagementProperty(val serializedName: String) {
    CanPause("canPause"),
    CanFreeze("canFreeze"),
    CanWipe("canWipe"),

    // ESDT only
    CanMint("canMint"),
    CanBurn("canBurn"),
    CanAddSpecialRoles("canAddSpecialRoles"),
    CanChangeOwner("canChangeOwner"),
    CanUpgrade("canUpgrade"),

    // NFT & SFT
    CanTransferNFTCreateRole("canTransferNFTCreateRole")

}
