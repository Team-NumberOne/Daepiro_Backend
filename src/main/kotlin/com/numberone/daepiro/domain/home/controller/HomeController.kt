package com.numberone.daepiro.domain.home.controller

import com.numberone.daepiro.domain.home.api.HomeApiV1
import com.numberone.daepiro.domain.home.dto.response.GetStatusResponse
import com.numberone.daepiro.domain.home.dto.response.GetWarningResponse
import com.numberone.daepiro.domain.home.dto.response.HomeDisasterFeed
import com.numberone.daepiro.domain.home.service.HomeService
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.utils.SecurityContextUtils
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController(
    private val homeService: HomeService
) : HomeApiV1 {
    override fun getHomeDisasters(): ApiResult<List<HomeDisasterFeed>> {
        val userId = SecurityContextUtils.getUserId()
        return homeService.getHomeDisasters(userId)
    }

    override fun getWarning(): ApiResult<GetWarningResponse> {
        val userId = SecurityContextUtils.getUserId()
        return homeService.getWarning(userId)
    }

    override fun getStatus(): ApiResult<GetStatusResponse> {
        TODO("Not yet implemented")
    }
}
