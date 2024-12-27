package com.numberone.daepiro.domain.community.dto.response

import com.numberone.daepiro.domain.address.entity.Address

data class UserAddressVerifiedResponse(
    val addressId: Long,
    val fullAddress: String,
    val isVerified: Boolean,
) {
    companion object {
        fun of(
            address: Address,
            isVerified: Boolean,
        ): UserAddressVerifiedResponse {
            return UserAddressVerifiedResponse(
                addressId = address.id!!,
                fullAddress = address.toFullAddress(),
                isVerified = isVerified,
            )
        }
    }
}
