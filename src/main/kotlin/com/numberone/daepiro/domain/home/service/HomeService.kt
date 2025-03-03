package com.numberone.daepiro.domain.home.service

import com.numberone.daepiro.domain.community.dto.request.GetArticleRequest
import com.numberone.daepiro.domain.community.dto.response.ArticleListResponse
import com.numberone.daepiro.domain.community.service.ArticleService
import com.numberone.daepiro.domain.dataCollecter.repository.NewsRepository
import com.numberone.daepiro.domain.disaster.service.DisasterService
import com.numberone.daepiro.domain.disasterContent.dto.response.DisasterContentResponse
import com.numberone.daepiro.domain.disasterContent.dto.response.GetHomeDisasterContentsResponse
import com.numberone.daepiro.domain.home.dto.request.GetHomeArticleRequest
import com.numberone.daepiro.domain.home.dto.response.GetStatusResponse
import com.numberone.daepiro.domain.home.dto.response.GetWarningResponse
import com.numberone.daepiro.domain.home.dto.response.GetWeatherResponse
import com.numberone.daepiro.domain.home.dto.response.HomeDisasterFeed
import com.numberone.daepiro.domain.user.dto.request.UpdateGpsRequest
import com.numberone.daepiro.domain.user.repository.UserRepository
import com.numberone.daepiro.domain.user.repository.findByIdOrThrow
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.exception.CustomErrorContext.NOT_FOUND_OCCURRED_DISASTER
import com.numberone.daepiro.global.exception.CustomException
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class HomeService(
    private val disasterService: DisasterService,
    private val userRepository: UserRepository,
    private val newsRepository: NewsRepository,
    private val articleService: ArticleService
) {
    fun getHomeDisasters(userId: Long): ApiResult<List<HomeDisasterFeed>> {
        val user = userRepository.findByIdOrThrow(userId)
        val disasters = disasterService.getDisasterByAddressAndType(
            user.userAddresses.map { it.address },
            user.userDisasterTypes.map { it.disasterType }
        )

        return ApiResult.ok(
            disasters
                .map { HomeDisasterFeed.from(it) }
                .take(3)
        )
    }

    fun getWarning(userId: Long): ApiResult<GetWarningResponse> {
        val user = userRepository.findByIdOrThrow(userId)
        val disasters = disasterService.getDisasterByAddressAndType(
            user.userAddresses.map { it.address }+user.address!!,
            user.userDisasterTypes.map { it.disasterType }
        )
        val twentyFourHoursAgo = LocalDateTime.now().minusHours(24)
        val disaster = disasters.firstOrNull()
            ?: throw CustomException(NOT_FOUND_OCCURRED_DISASTER)
        if (disaster.generatedAt.isBefore(twentyFourHoursAgo))
            throw CustomException(NOT_FOUND_OCCURRED_DISASTER)

        return ApiResult.ok(GetWarningResponse.from(disaster))
    }

    fun getStatus(userId: Long): ApiResult<GetStatusResponse> {
        try {
            getWarning(userId)
        } catch (e: CustomException) {
            return ApiResult.ok(GetStatusResponse(false))
        }
        return ApiResult.ok(GetStatusResponse(true))
    }

    fun getHomeNews(): ApiResult<GetHomeDisasterContentsResponse> {
        val newsList = newsRepository.findTop15ByOrderByPublishedAtDesc()
        return ApiResult.ok(GetHomeDisasterContentsResponse.of(newsList))
    }

    fun getHomeArticles(userId: Long, request: GetHomeArticleRequest): ApiResult<List<ArticleListResponse>> {
        val user = userRepository.findByIdOrThrow(userId)
        return ApiResult.ok(
            articleService.getHomeFeed(GetArticleRequest.ofHomeFeed(request.category, user), userId)
        )
    }

    fun getWeather(userId: Long): ApiResult<GetWeatherResponse> {
        val user = userRepository.findByIdOrThrow(userId)
        val address = user.address
            ?: user.userAddresses.first { it.name == "집" }.address
        return ApiResult.ok(
            GetWeatherResponse.of(address.toHomeAddress(), "맑음")
            //todo 날씨 api 연동
        )
    }
}
