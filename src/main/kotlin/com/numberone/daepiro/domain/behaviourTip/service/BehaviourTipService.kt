package com.numberone.daepiro.domain.behaviourTip.service

import com.numberone.daepiro.domain.behaviourTip.dto.response.BehaviourTipDisasterResponse
import com.numberone.daepiro.domain.behaviourTip.repository.BehaviourTipRepository
import com.numberone.daepiro.domain.disaster.entity.DisasterType
import com.numberone.daepiro.domain.disaster.enums.DisasterLevel
import com.numberone.daepiro.domain.disaster.repository.DisasterTypeRepository
import com.numberone.daepiro.global.dto.ApiResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class BehaviourTipService(
    private val behaviourTipRepository: BehaviourTipRepository,
    private val disasterTypeRepository: DisasterTypeRepository
) {
    fun getEmergencyTips(): ApiResult<List<BehaviourTipDisasterResponse>> {
        val disasterTypes = disasterTypeRepository.findByLevel(DisasterLevel.EMERGENCY)

        return ApiResult.ok(
            sortTypesByKorean(disasterTypes)
                .map { BehaviourTipDisasterResponse.of(it) }
        )
    }

    fun getCommonTips(): ApiResult<List<BehaviourTipDisasterResponse>> {
        val disasterTypes = disasterTypeRepository.findByLevel(DisasterLevel.COMMON)

        return ApiResult.ok(
            sortTypesByKorean(disasterTypes)
                .map { BehaviourTipDisasterResponse.of(it) }
        )
    }

    private fun sortTypesByKorean(tips: List<DisasterType>): List<DisasterType> {
        return tips.sortedBy { it.type.korean }
    }
}
