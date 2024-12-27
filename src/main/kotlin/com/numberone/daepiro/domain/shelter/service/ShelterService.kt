package com.numberone.daepiro.domain.shelter.service

import com.numberone.daepiro.domain.shelter.dto.response.GetNearbySheltersResponse
import com.numberone.daepiro.domain.shelter.entity.ShelterType
import com.numberone.daepiro.domain.shelter.repository.ShelterRepository
import com.numberone.daepiro.domain.user.repository.UserRepository
import com.numberone.daepiro.domain.user.repository.findByIdOrThrow
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.exception.CustomErrorContext
import com.numberone.daepiro.global.exception.CustomException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ShelterService(
    private val shelterRepository: ShelterRepository,
    private val userRepository: UserRepository,
) {
    fun getNearbyShelters(userId: Long, type: String): ApiResult<GetNearbySheltersResponse> {
        val user = userRepository.findByIdOrThrow(userId)
        if (user.address == null || user.longitude == null || user.latitude == null) {
            throw CustomException(CustomErrorContext.NOT_FOUND_USER_LOCATION)
        }
        val address = user.address!!
        val longitude = user.longitude!!
        val latitude = user.latitude!!

        val shelters = shelterRepository.findTop10ClosestShelters(longitude, latitude, ShelterType.word2code(type).name)
        return ApiResult.ok(
            GetNearbySheltersResponse.of(
                address.toFullAddress(),
                shelters,
                longitude,
                latitude
            )
        )
    }
}
