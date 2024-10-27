package com.numberone.daepiro.domain.home.service

import com.numberone.daepiro.domain.disaster.service.DisasterService
import com.numberone.daepiro.domain.home.dto.response.GetStatusResponse
import com.numberone.daepiro.domain.home.dto.response.GetWarningResponse
import com.numberone.daepiro.domain.home.dto.response.HomeDisasterFeed
import com.numberone.daepiro.domain.user.repository.UserRepository
import com.numberone.daepiro.domain.user.repository.findByIdOrThrow
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.exception.CustomErrorContext.NOT_FOUND_OCCURRED_DISASTER
import com.numberone.daepiro.global.exception.CustomException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class HomeService(
    private val disasterService: DisasterService,
    private val userRepository: UserRepository
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
            user.userAddresses.map { it.address },
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
}
