package com.numberone.daepiro.domain.behaviourTip.controller

import com.numberone.daepiro.domain.behaviourTip.api.BehaviourTipApiV1
import com.numberone.daepiro.domain.behaviourTip.dto.request.CreateTipRequest
import com.numberone.daepiro.domain.behaviourTip.dto.response.BehaviourTipDisasterResponse
import com.numberone.daepiro.domain.behaviourTip.dto.response.GetBehaviourTipResponse
import com.numberone.daepiro.domain.disaster.enums.DisasterLevel
import com.numberone.daepiro.domain.behaviourTip.service.BehaviourTipService
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.exception.CustomErrorContext
import com.numberone.daepiro.global.exception.CustomException
import org.springframework.web.bind.annotation.RestController

@RestController
class BehaviourTipController(
    private val behaviourTipService: BehaviourTipService
) : BehaviourTipApiV1 {
    override fun getBehaviourTips(
        type: String
    ): ApiResult<List<BehaviourTipDisasterResponse>> {
        return when (type.uppercase()) {
            DisasterLevel.EMERGENCY.name -> behaviourTipService.getEmergencyTips()
            DisasterLevel.COMMON.name -> behaviourTipService.getCommonTips()
            else -> throw CustomException(CustomErrorContext.NOT_FOUND_TIP_TYPE)
        }
    }

    override fun searchBehaviourTips(keyword: String): ApiResult<List<BehaviourTipDisasterResponse>> {
        return behaviourTipService.searchTips(keyword)
    }

    override fun createBehaviourTip(request: CreateTipRequest): ApiResult<Unit>{
        behaviourTipService.createTip(request)
        return ApiResult.ok()
    }

    override fun getBehaviourTip(disasterId: Long): ApiResult<GetBehaviourTipResponse> {
        return behaviourTipService.getBehaviourTip(disasterId)
    }
}
