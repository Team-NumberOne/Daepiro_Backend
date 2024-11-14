package com.numberone.daepiro.domain.disasterContent.controller

import com.numberone.daepiro.domain.disasterContent.api.DisasterContentApiV1
import com.numberone.daepiro.domain.disasterContent.dto.response.GetDisasterContentsResponse
import com.numberone.daepiro.domain.disasterContent.service.DisasterContentService
import com.numberone.daepiro.global.dto.ApiResult
import org.springframework.web.bind.annotation.RestController

@RestController
class DisasterContentController(
    val disasterContentService: DisasterContentService
) : DisasterContentApiV1 {
    override fun getDisasterContents(
        sortType: String,
        cursor: Long?,
        size: Long
    ): ApiResult<GetDisasterContentsResponse> {
        return disasterContentService.getDisasterContents(sortType, cursor, size)
    }

    override fun searchDisasterContents(
        keyword: String,
        cursor: Long?,
        size: Long
    ): ApiResult<GetDisasterContentsResponse> {
        return disasterContentService.searchDisasterContents(keyword, cursor, size)
    }
}
