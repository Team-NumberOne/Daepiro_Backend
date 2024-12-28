package com.numberone.daepiro.domain.home.controller

import com.numberone.daepiro.domain.community.dto.request.GetArticleRequest
import com.numberone.daepiro.domain.community.dto.response.ArticleListResponse
import com.numberone.daepiro.domain.community.service.ArticleService
import com.numberone.daepiro.domain.disasterContent.dto.response.DisasterContentResponse
import com.numberone.daepiro.domain.disasterContent.dto.response.GetHomeDisasterContentsResponse
import com.numberone.daepiro.domain.home.api.HomeApiV1
import com.numberone.daepiro.domain.home.dto.request.GetHomeArticleRequest
import com.numberone.daepiro.domain.home.dto.response.GetStatusResponse
import com.numberone.daepiro.domain.home.dto.response.GetWarningResponse
import com.numberone.daepiro.domain.home.dto.response.HomeDisasterFeed
import com.numberone.daepiro.domain.home.service.HomeService
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.utils.SecurityContextUtils
import org.springframework.data.domain.Slice
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController(
    private val homeService: HomeService,
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
}
