package com.numberone.daepiro.domain.user.service

import com.numberone.daepiro.domain.address.entity.UserAddress
import com.numberone.daepiro.domain.address.repository.AddressRepository
import com.numberone.daepiro.domain.address.repository.GeoLocationConverter
import com.numberone.daepiro.domain.address.repository.UserAddressRepository
import com.numberone.daepiro.domain.address.vo.AddressInfo
import com.numberone.daepiro.domain.disaster.entity.DisasterType
import com.numberone.daepiro.domain.disaster.entity.UserDisasterType
import com.numberone.daepiro.domain.disaster.repository.DisasterTypeRepository
import com.numberone.daepiro.domain.disaster.repository.UserDisasterTypeRepository
import com.numberone.daepiro.domain.disaster.repository.findByTypeOrThrow
import com.numberone.daepiro.domain.disaster.service.DisasterService
import com.numberone.daepiro.domain.user.dto.request.AddressRequest
import com.numberone.daepiro.domain.user.dto.request.OnboardingRequest
import com.numberone.daepiro.domain.user.dto.request.UpdateGpsRequest
import com.numberone.daepiro.domain.user.dto.response.CheckNicknameResponse
import com.numberone.daepiro.domain.user.dto.response.DisasterWithRegionResponse
import com.numberone.daepiro.domain.user.dto.response.UserAddressResponse
import com.numberone.daepiro.domain.user.entity.UserEntity
import com.numberone.daepiro.domain.user.repository.UserRepository
import com.numberone.daepiro.domain.user.repository.findByIdOrThrow
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.exception.CustomErrorContext.INVALID_ADDRESS_FORMAT
import com.numberone.daepiro.global.exception.CustomException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val userAddressRepository: UserAddressRepository,
    private val userDisasterTypeRepository: UserDisasterTypeRepository,
    private val disasterTypeRepository: DisasterTypeRepository,
    private val addressRepository: AddressRepository,
    private val disasterService: DisasterService,
    private val geoLocationConverter: GeoLocationConverter,
) {
    fun checkNickname(
        nickname: String,
    ): ApiResult<CheckNicknameResponse> {
        val user = userRepository.findByNicknameAndDeletedAtIsNull(nickname)
        return ApiResult.ok(CheckNicknameResponse.from(user == null))
    }

    @Transactional
    fun setOnboardingData(
        request: OnboardingRequest,
        userId: Long,
    ): ApiResult<Unit> {
        val user = userRepository.findByIdOrThrow(userId)
        user.initName(request.realname, request.nickname)
        user.initFcmToken(request.fcmToken)
        handleOnboardingAddress(request.addresses, user)
        handleOnboardingDisasterType(request.disasterTypes, user)
        user.completeOnboarding()
        return ApiResult.ok()
    }

    private fun handleOnboardingDisasterType(
        disasterTypes: List<String>,
        user: UserEntity,
    ) {
        userDisasterTypeRepository.deleteAllByUser(user)

        val userDisasterTypeList = mutableListOf<UserDisasterType>()
        for (str in disasterTypes) {
            val disasterType = disasterTypeRepository.findByTypeOrThrow(DisasterType.DisasterValue.kor2code(str))
            userDisasterTypeList.add(
                UserDisasterType.of(
                    user = user,
                    disasterType = disasterType
                )
            )
        }
        userDisasterTypeRepository.saveAll(userDisasterTypeList)
    }

    private fun handleOnboardingAddress(
        addresses: List<AddressRequest>,
        user: UserEntity,
    ) {
        userAddressRepository.deleteAllByUser(user)

        val userAddressList = mutableListOf<UserAddress>()
        for (addressReq in addresses) {
            val addressInfo = AddressInfo.from(addressReq.address)
            val addressEntity = addressRepository.findByAddressInfo(addressInfo)
                ?: throw CustomException(INVALID_ADDRESS_FORMAT)
            userAddressList.add(
                UserAddress.of(
                    name = addressReq.name,
                    user = user,
                    address = addressEntity
                )
            )
        }
        userAddressRepository.saveAll(userAddressList)
    }

    @Transactional
    fun updateGps(
        request: UpdateGpsRequest,
        userId: Long,
    ): ApiResult<Unit> {
        val address = geoLocationConverter.findByLongitudeAndLatitudeOrThrow(request.longitude, request.latitude)
        val user = userRepository.findByIdOrThrow(userId)

        user.updateLocation(address, request.longitude, request.latitude)
        return ApiResult.ok()
    }

    fun getRecentDisasters(userId: Long): ApiResult<List<DisasterWithRegionResponse>> {
        val user = userRepository.findByIdOrThrow(userId)
        val desiredDisasterTypes = user.userDisasterTypes.map { it.disasterType }
        val allAddresses = user.userAddresses.map { it.address }
        val result = mutableListOf<DisasterWithRegionResponse>()

        result.add(
            DisasterWithRegionResponse.of(
                "전체",
                disasterService.getDisasterByAddressAndType(allAddresses, desiredDisasterTypes)
            )
        )
        for (ua in user.userAddresses) {
            val disasters = disasterService.getDisasterByAddressAndType(ua.address, desiredDisasterTypes)
            result.add(
                DisasterWithRegionResponse.of(
                    ua.name,
                    disasters
                )
            )
        }
        return ApiResult.ok(result)
    }

    fun getAddresses(
        userId: Long
    ): ApiResult<List<UserAddressResponse>> {
        val user = userRepository.findByIdOrThrow(userId)
        return ApiResult.ok(user.userAddresses.map {
            UserAddressResponse.of(it.address)
        })
    }
}
