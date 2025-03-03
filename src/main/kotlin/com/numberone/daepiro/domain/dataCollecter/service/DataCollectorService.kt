package com.numberone.daepiro.domain.dataCollecter.service

import com.numberone.daepiro.domain.address.repository.AddressRepository
import com.numberone.daepiro.domain.address.repository.UserAddressRepository
import com.numberone.daepiro.domain.address.vo.AddressInfo
import com.numberone.daepiro.domain.dataCollecter.dto.request.SaveDisastersRequest
import com.numberone.daepiro.domain.dataCollecter.dto.request.SaveNewsRequest
import com.numberone.daepiro.domain.dataCollecter.dto.response.GetLatestDisasterResponse
import com.numberone.daepiro.domain.dataCollecter.dto.response.GetLatestNewsResponse
import com.numberone.daepiro.domain.dataCollecter.entity.News
import com.numberone.daepiro.domain.dataCollecter.repository.NewsRepository
import com.numberone.daepiro.domain.disaster.entity.Disaster
import com.numberone.daepiro.domain.disaster.entity.DisasterType.DisasterValue
import com.numberone.daepiro.domain.disaster.repository.DisasterRepository
import com.numberone.daepiro.domain.disaster.repository.DisasterTypeRepository
import com.numberone.daepiro.domain.disasterSituation.service.DisasterSituationService
import com.numberone.daepiro.domain.notification.entity.NotificationCategory
import com.numberone.daepiro.domain.notification.service.NotificationService
import com.numberone.daepiro.domain.user.repository.UserRepository
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.exception.CustomErrorContext.INVALID_ADDRESS_FORMAT
import com.numberone.daepiro.global.exception.CustomErrorContext.NOT_FOUND_DISASTER_TYPE
import com.numberone.daepiro.global.exception.CustomException
import com.numberone.daepiro.global.utils.FcmUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class DataCollectorService(
    private val newsRepository: NewsRepository,
    private val disasterRepository: DisasterRepository,
    private val addressRepository: AddressRepository,
    private val disasterTypeRepository: DisasterTypeRepository,
    private val userAddressRepository: UserAddressRepository,
    private val disasterSituationService: DisasterSituationService,
    private val notificationService: NotificationService,
    private val userRepository: UserRepository
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
        val disaster = disasterRepository.findLatestDisaster().firstOrNull()

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
                address = addressRepository.findByAddressInfo(AddressInfo.from(it.locationStr))
                    ?: throw CustomException(INVALID_ADDRESS_FORMAT),
                disasterType = disasterTypeRepository.findByType(DisasterValue.kor2code(it.disasterType))
                    ?: throw CustomException(NOT_FOUND_DISASTER_TYPE),
            )
        }

        disasterRepository.saveAll(disasters)
        disasterSituationService.createDisasterSituation(disasters)
        sendDisasterNotification(disasters)
        // todo fcm 적용시 주석해제
    }

    private fun sendDisasterNotification(disasters: List<Disaster>) {
        for (disaster in disasters) {
            val addressInfo = AddressInfo.from(disaster.address)
            val currentAddress = addressRepository.findByAddressInfo(addressInfo)
                ?: throw CustomException(INVALID_ADDRESS_FORMAT)
            val addresses = addressRepository.findChildAddress(addressInfo) + currentAddress
            val users = userAddressRepository
                .findByAddressIdIn(addresses.map { it.id!! })
                .map { it.user }
            val gpsUsers = userRepository.findByAddressIdIn(addresses.map { it.id!! })
            val uniqueUsers = (users + gpsUsers).distinctBy { it.id!! }

            notificationService.sendNotification(
                uniqueUsers,
                NotificationCategory.DISASTER,
                "${disaster.address.toFullAddress()} ${disaster.disasterType.type.korean} 발생",
                "대피로에서 ${disaster.disasterType.type.korean} 행동요령을 확인해보세요."
            )
        }
    }
}
