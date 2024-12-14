package com.numberone.daepiro.domain.disasterSituation.controller

import com.numberone.daepiro.domain.disasterSituation.api.DisasterSituationApiV1
import com.numberone.daepiro.domain.disasterSituation.dto.request.CreateSituationCommentRequest
import com.numberone.daepiro.domain.disasterSituation.dto.request.EditSituationCommentRequest
import com.numberone.daepiro.domain.disasterSituation.dto.response.DisasterSituationResponse
import com.numberone.daepiro.domain.disasterSituation.dto.response.SituationCommentResponse
import com.numberone.daepiro.domain.disasterSituation.service.DisasterSituationService
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.utils.SecurityContextUtils
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class DisasterSituationController(
    private val disasterSituationService: DisasterSituationService
) : DisasterSituationApiV1 {
    override fun getDisasterSituations(): ApiResult<List<DisasterSituationResponse>> {
        val userId = SecurityContextUtils.getUserId()
        return disasterSituationService.getDisasterSituations(userId)
    }

    override fun getComments(situationId: Long): ApiResult<List<SituationCommentResponse>> {
        val userId = SecurityContextUtils.getUserId()
        return disasterSituationService.getComments(userId, situationId)
    }
}
