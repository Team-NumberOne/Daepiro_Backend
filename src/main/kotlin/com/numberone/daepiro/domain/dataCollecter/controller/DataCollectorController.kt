package com.numberone.daepiro.domain.dataCollecter.controller

import com.numberone.daepiro.domain.dataCollecter.api.DataCollectorApiV1
import com.numberone.daepiro.domain.dataCollecter.dto.request.SaveDisastersRequest
import com.numberone.daepiro.domain.dataCollecter.dto.request.SaveNewsRequest
import com.numberone.daepiro.domain.dataCollecter.dto.response.GetLatestDisasterResponse
import com.numberone.daepiro.domain.dataCollecter.dto.response.GetLatestNewsResponse
import com.numberone.daepiro.domain.dataCollecter.service.DataCollectorService
import com.numberone.daepiro.global.dto.ApiResult
import org.springframework.web.bind.annotation.RestController

@RestController
class DataCollectorController(
    val dataCollectorService: DataCollectorService
) : DataCollectorApiV1 {
    override fun getLatestNews(
    ): ApiResult<GetLatestNewsResponse> {
        return dataCollectorService.getLatestNews()
    }

    override fun getLatestDisasters(
    ): ApiResult<GetLatestDisasterResponse> {
        return dataCollectorService.getLatestDisasters()
    }

    override fun saveNews(
        request: SaveNewsRequest
    ): ApiResult<Unit> {
        dataCollectorService.saveNews(request)
        return ApiResult.ok()
    }

    override fun saveDisasters(
        request: SaveDisastersRequest
    ): ApiResult<Unit> {
        dataCollectorService.saveDisasters(request)
        return ApiResult.ok()
    }
}
