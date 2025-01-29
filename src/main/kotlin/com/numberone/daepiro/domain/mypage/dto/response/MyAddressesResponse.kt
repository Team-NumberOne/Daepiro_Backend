package com.numberone.daepiro.domain.mypage.dto.response

import com.numberone.daepiro.domain.address.entity.UserAddress
import com.numberone.daepiro.domain.user.entity.UserEntity
import io.swagger.v3.oas.annotations.media.Schema

data class MyAddressesResponse(
    val addresses: List<AddressResponse>
) {
    companion object {
        fun of(
            user: UserEntity
        ): MyAddressesResponse {
            return MyAddressesResponse(
                addresses = user.userAddresses.map { AddressResponse.of(it) }
            )
        }
    }
}

data class AddressResponse(
    @Schema(description = "이름", example = "집")
    val name: String,

    @Schema(description = "주소", example = "서울특별시 영등포구 신길동")
    val address: String
) {
    companion object {
        fun of(
            address: UserAddress
        ): AddressResponse {
            return AddressResponse(
                name = address.name,
                address = address.address.toFullAddress()
            )
        }
    }
}
