package com.numberone.daepiro.domain.mypage.dto.response

import com.numberone.daepiro.domain.user.entity.UserEntity
import io.swagger.v3.oas.annotations.media.Schema

data class MyDisasterTypesResponse(
    @Schema(
        description = "재난 유형",
        example = "[\"지진\", \"화재\", \"태풍\"]",
    )
    val disasterTypes: List<String>
) {
    companion object {
        fun of(
            user: UserEntity
        ): MyDisasterTypesResponse {
            return MyDisasterTypesResponse(
                disasterTypes = user.userDisasterTypes.map { it.disasterType.type.korean }
            )
        }
    }
}
