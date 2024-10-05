package com.numberone.daepiro.domain.dataCollecter.service

import com.numberone.daepiro.domain.dataCollecter.dto.request.SaveDisastersRequest
import com.numberone.daepiro.domain.dataCollecter.dto.request.SaveNewsRequest
import com.numberone.daepiro.domain.dataCollecter.dto.response.GetLatestDisasterResponse
import com.numberone.daepiro.domain.dataCollecter.dto.response.GetLatestNewsResponse
import com.numberone.daepiro.domain.dataCollecter.entity.News
import com.numberone.daepiro.domain.dataCollecter.repository.NewsRepository
import com.numberone.daepiro.domain.disaster.entity.Disaster
import com.numberone.daepiro.domain.disaster.repository.DisasterRepository
import com.numberone.daepiro.global.dto.ApiResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class DataCollectorService(
    private val newsRepository: NewsRepository,
    private val disasterRepository: DisasterRepository
) {
    fun getLatestNews(): ApiResult<GetLatestNewsResponse> {
        val news = newsRepository.findLatestNews().firstOrNull()

        return ApiResult.ok(
            GetLatestNewsResponse.from(
                publishedAt = news?.publishedAt
                    ?: LocalDateTime.of(2000, 1, 1, 0, 0)
            )
        )
    }

    fun getLatestDisasters(): ApiResult<GetLatestDisasterResponse> {
        val disaster = disasterRepository.findLatestDisaster()

        return ApiResult.ok(
            GetLatestDisasterResponse.from(
                messageId = disaster?.messageId ?: 0
            )
        )
    }

    @Transactional
    fun saveNews(
        request: SaveNewsRequest
    ) {
        val news = request.news.map {
            News.of(
                title = it.title,
                publishedAt = it.publishedAt,
                subtitle = it.subtitle,
                body = it.body,
                thumbnailUrl = it.thumbnailUrl
            )
        }

        newsRepository.saveAll(news)
    }

    @Transactional
    fun saveDisasters(
        request: SaveDisastersRequest
    ) {
        val disasters = request.disasters.map {
            Disaster.of(
                generatedAt = it.generatedAt,
                messageId = it.messageId,
                message = it.message,
                locationId = it.locationId
            )
        }

        disasterRepository.saveAll(disasters)
    }
}
