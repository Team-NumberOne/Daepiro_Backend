package com.numberone.daepiro.domain.shelter.dto.response

import com.numberone.daepiro.domain.shelter.entity.Shelter
import com.numberone.daepiro.domain.shelter.utils.DistanceUtils
import io.swagger.v3.oas.annotations.media.Schema

data class GetNearbySheltersResponse(
    @Schema(description = "현재 위치", example = "서울특별시 강남구 역삼동 123-45")
    val myLocation: String,

    val shelters: List<ShelterResponse>
) {
    companion object {
        fun of(
            myLocation: String,
            shelters: List<Shelter>,
            longitude: Double,
            latitude: Double
        ): GetNearbySheltersResponse {
            return GetNearbySheltersResponse(
                myLocation,
                shelters.map {
                    ShelterResponse.of(
                        it,
                        DistanceUtils.calculateDistance(
                            longitude,
                            latitude,
                            it.longitude,
                            it.latitude
                        )
                    )
                }.distinctBy { it.name }.take(10)
            )
        }
    }
}
