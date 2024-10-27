package com.numberone.daepiro.domain.home.service

import com.numberone.daepiro.domain.disaster.service.DisasterService
import com.numberone.daepiro.domain.home.dto.response.HomeDisasterFeed
import com.numberone.daepiro.domain.user.repository.UserRepository
import com.numberone.daepiro.domain.user.repository.findByIdOrThrow
import com.numberone.daepiro.global.dto.ApiResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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
}
