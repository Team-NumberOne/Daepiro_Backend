package com.numberone.daepiro.domain.mypage.dto.response

import com.numberone.daepiro.domain.address.entity.UserAddress
import com.numberone.daepiro.domain.user.entity.UserEntity
import io.swagger.v3.oas.annotations.media.Schema

data class MyAddressesResponse(
    val addresses: List<MyAddressReponse>
) {
    companion object {
        fun of(
            user: UserEntity
        ): MyAddressesResponse {
            return MyAddressesResponse(
                addresses = user.userAddresses.map { MyAddressReponse.of(it) }
            )
        }
    }
}

data class MyAddressReponse(
    @Schema(description = "이름", example = "집")
    val name: String,

    @Schema(description = "주소", example = "서울특별시 영등포구 신길동")
    val address: String
) {
    companion object {
        fun of(
            address: UserAddress
        ): MyAddressReponse {
            return MyAddressReponse(
                name = address.name,
                address = address.address.toFullAddress()
            )
        }
    }
}
