package com.numberone.daepiro.domain.shelter.controller

import com.numberone.daepiro.domain.shelter.api.ShelterApiV1
import com.numberone.daepiro.domain.shelter.dto.response.GetNearbySheltersResponse
import com.numberone.daepiro.domain.shelter.service.ShelterService
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.utils.SecurityContextUtils
import org.springframework.web.bind.annotation.RestController

@RestController
class ShelterController(
    private val shelterService: ShelterService
) : ShelterApiV1 {
    override fun getNearbyShelters(type: String): ApiResult<GetNearbySheltersResponse> {
        val userId = SecurityContextUtils.getUserId()
        return shelterService.getNearbyShelters(userId, type)
    }
}
