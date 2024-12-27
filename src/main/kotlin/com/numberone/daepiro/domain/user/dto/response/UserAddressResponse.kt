package com.numberone.daepiro.domain.user.dto.response

import com.numberone.daepiro.domain.address.entity.Address

data class UserAddressResponse(
    val addressId: Long,
    val fullAddress: String,
    val shortAddress: String
) {
    companion object {
        fun of(
            address: Address
        ): UserAddressResponse {
            return UserAddressResponse(
                addressId = address.id!!,
                fullAddress = address.toFullAddress(),
                shortAddress = address.toShortAddress()
            )
        }
    }
}
