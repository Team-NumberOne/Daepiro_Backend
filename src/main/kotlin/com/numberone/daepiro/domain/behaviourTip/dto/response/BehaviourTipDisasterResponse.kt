package com.numberone.daepiro.domain.behaviourTip.dto.response

import com.numberone.daepiro.domain.disaster.entity.Disaster
import com.numberone.daepiro.domain.disaster.entity.DisasterType

data class BehaviourTipDisasterResponse(
    val id: Long,
    val name: String,
) {
    companion object {
        fun of(
            disasterType: DisasterType
        ): BehaviourTipDisasterResponse {
            return BehaviourTipDisasterResponse(
                id = disasterType.id!!,
                name = disasterType.type.korean,
            )
        }
    }
}
