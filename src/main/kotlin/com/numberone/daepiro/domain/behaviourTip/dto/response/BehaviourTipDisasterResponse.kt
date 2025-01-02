package com.numberone.daepiro.domain.behaviourTip.dto.response

import com.numberone.daepiro.domain.disaster.entity.Disaster
import com.numberone.daepiro.domain.disaster.entity.DisasterType

data class BehaviourTipDisasterResponse(
    val id: Long,
    val name: String,
    val tips: List<FilterResponse>
) {
    companion object {
        fun of(
            disasterType: DisasterType,
            tips: List<FilterResponse>
        ): BehaviourTipDisasterResponse {
            return BehaviourTipDisasterResponse(
                id = disasterType.id!!,
                name = disasterType.type.korean,
                tips = tips
            )
        }
    }
}
