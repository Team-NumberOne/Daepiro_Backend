package com.numberone.daepiro.domain.user.dto.response

import com.numberone.daepiro.domain.disaster.entity.Disaster

data class DisasterWithRegionResponse(
    val region: String,
    val disasters: List<DisasterResponse>
) {
    companion object {
        fun of(
            region: String,
            disasters: List<Disaster>
        ): DisasterWithRegionResponse {
            return DisasterWithRegionResponse(
                region = region,
                disasters = disasters.map { DisasterResponse.of(it) }
            )
        }
    }
}
