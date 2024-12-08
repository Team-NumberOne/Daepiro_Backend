package com.numberone.daepiro.domain.disasterContent.service

import com.numberone.daepiro.domain.dataCollecter.repository.NewsRepository
import com.numberone.daepiro.domain.disasterContent.dto.response.DisasterContentsResponse
import com.numberone.daepiro.domain.disasterContent.dto.response.GetHomeDisasterContentsResponse
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.exception.CustomErrorContext.INVALID_DISASTER_CONTENT_SORT_TYPE
import com.numberone.daepiro.global.exception.CustomException
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class DisasterContentService(
    val newsRepository: NewsRepository
) {
    fun getDisasterContents(
        sortType: String,
        cursor: Long?,
        size: Long
    ): ApiResult<DisasterContentsResponse> {
        val pageable = PageRequest.of(0, size.toInt())
        val currentCursor = cursor
            ?: newsRepository.findLatestNews().firstOrNull()?.id
            ?: return ApiResult.ok(DisasterContentsResponse.of(emptyList()))

        val newsList = when (sortType) {
            "latest" -> {
                newsRepository.findNewsByCursor(currentCursor, pageable)
            }

            "popular" -> {
                // todo(좋아요 도메인이 만들어진 후에는 sortType이 popular일 때 좋아요 순으로 정렬)
                newsRepository.findNewsByCursor(currentCursor, pageable)
            }

            else -> {
                throw CustomException(INVALID_DISASTER_CONTENT_SORT_TYPE)
            }
        }

        return ApiResult.ok(DisasterContentsResponse.of(newsList))
    }

    fun searchDisasterContents(
        keyword: String,
        cursor: Long?,
        size: Long
    ): ApiResult<DisasterContentsResponse> {
        val pageable = PageRequest.of(0, size.toInt())
        val currentCursor = cursor
            ?: newsRepository.findLatestNews().firstOrNull()?.id
            ?: return ApiResult.ok(DisasterContentsResponse.of(emptyList()))

        val newsList = newsRepository.searchNews(keyword, currentCursor, pageable)

        return ApiResult.ok(DisasterContentsResponse.of(newsList))
    }

    fun getHomeDisasterContents(): ApiResult<GetHomeDisasterContentsResponse> {
        val newsList = newsRepository.findTop3ByOrderByPublishedAtDesc()
        return ApiResult.ok(GetHomeDisasterContentsResponse.of(newsList))
    }

}
