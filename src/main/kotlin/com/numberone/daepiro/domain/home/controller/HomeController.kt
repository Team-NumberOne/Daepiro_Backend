package com.numberone.daepiro.domain.home.controller

import com.numberone.daepiro.domain.behaviourTip.dto.response.GetBehaviourTipResponse
import com.numberone.daepiro.domain.behaviourTip.service.BehaviourTipService
import com.numberone.daepiro.domain.community.dto.request.GetArticleRequest
import com.numberone.daepiro.domain.community.dto.response.ArticleListResponse
import com.numberone.daepiro.domain.community.service.ArticleService
import com.numberone.daepiro.domain.disasterContent.dto.response.DisasterContentResponse
import com.numberone.daepiro.domain.disasterContent.dto.response.GetHomeDisasterContentsResponse
import com.numberone.daepiro.domain.home.api.HomeApiV1
import com.numberone.daepiro.domain.home.dto.request.GetHomeArticleRequest
import com.numberone.daepiro.domain.home.dto.response.GetStatusResponse
import com.numberone.daepiro.domain.home.dto.response.GetWarningResponse
import com.numberone.daepiro.domain.home.dto.response.GetWeatherResponse
import com.numberone.daepiro.domain.home.dto.response.HomeDisasterFeed
import com.numberone.daepiro.domain.home.service.HomeService
import com.numberone.daepiro.domain.shelter.dto.response.GetNearbySheltersResponse
import com.numberone.daepiro.domain.shelter.service.ShelterService
import com.numberone.daepiro.domain.user.dto.request.UpdateGpsRequest
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.utils.SecurityContextUtils
import org.springframework.data.domain.Slice
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController(
    private val homeService: HomeService,
    private val shelterService: ShelterService,
    private val behaviourTipService: BehaviourTipService
) : HomeApiV1 {
    override fun getHomeDisasters(): ApiResult<List<HomeDisasterFeed>> {
        val userId = SecurityContextUtils.getUserId()
        return homeService.getHomeDisasters(userId)
    }

    override fun getHomeNews(): ApiResult<GetHomeDisasterContentsResponse> {
        return homeService.getHomeNews()
    }

    override fun getHomeArticles(request: GetHomeArticleRequest): ApiResult<List<ArticleListResponse>> {
        val userId = SecurityContextUtils.getUserId()
        return homeService.getHomeArticles(userId, request)
    }

    override fun getWarning(): ApiResult<GetWarningResponse> {
        val userId = SecurityContextUtils.getUserId()
        return homeService.getWarning(userId)
    }

    override fun getStatus(): ApiResult<GetStatusResponse> {
        val userId = SecurityContextUtils.getUserId()
        return homeService.getStatus(userId)
    }

    override fun getShelters(type: String): ApiResult<GetNearbySheltersResponse> {
        val userId = SecurityContextUtils.getUserId()
        return shelterService.getNearbyShelters(userId, type)
    }

    override fun getBehaviourTip(disasterId: Long): ApiResult<GetBehaviourTipResponse> {
        return behaviourTipService.getBehaviourTip(disasterId)
    }

    override fun getWeather(): ApiResult<GetWeatherResponse> {
        val userId = SecurityContextUtils.getUserId()
        return homeService.getWeather(userId)
    }
}
