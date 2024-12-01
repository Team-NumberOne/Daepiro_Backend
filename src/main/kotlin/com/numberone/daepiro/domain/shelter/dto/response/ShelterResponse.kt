package com.numberone.daepiro.domain.shelter.dto.response

import com.numberone.daepiro.domain.shelter.entity.Shelter
import io.swagger.v3.oas.annotations.media.Schema

data class ShelterResponse(
    @Schema(description = "대피소 이름", example = "EG스위트빌경로당(금호2동)")
    val name: String,

    @Schema(description = "대피소까지의 거리", example = "250")
    val distance: Long,

    @Schema(description = "대피소 주소", example = "광주광역시 서구 화개중앙로87번길 16-1 (금호동, EG스위트밸리)")
    val address: String,

    @Schema(description = "대피소 경도", example = "126.8509506")
    val longitude: Double,

    @Schema(description = "대피소 위도", example = "35.1277754")
    val latitude: Double
) {
    companion object {
        fun of(shelter: Shelter, distance: Long): ShelterResponse {
            return ShelterResponse(
                name = shelter.name,
                distance = distance,
                address = shelter.address,
                longitude = shelter.longitude,
                latitude = shelter.latitude
            )
        }
    }
}
