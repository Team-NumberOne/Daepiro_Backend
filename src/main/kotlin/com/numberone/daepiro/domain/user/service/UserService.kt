package com.numberone.daepiro.domain.user.service

import com.numberone.daepiro.domain.address.entity.Address
import com.numberone.daepiro.domain.address.entity.KoreaLocation
import com.numberone.daepiro.domain.address.entity.UserAddress
import com.numberone.daepiro.domain.address.repository.AddressRepository
import com.numberone.daepiro.domain.address.repository.KoreaLocationRepository
import com.numberone.daepiro.domain.address.repository.UserAddressRepository
import com.numberone.daepiro.domain.address.vo.AddressInfo
import com.numberone.daepiro.domain.disaster.entity.DisasterType
import com.numberone.daepiro.domain.disaster.entity.UserDisasterType
import com.numberone.daepiro.domain.disaster.repository.DisasterTypeRepository
import com.numberone.daepiro.domain.disaster.repository.UserDisasterTypeRepository
import com.numberone.daepiro.domain.disaster.repository.findByTypeOrThrow
import com.numberone.daepiro.domain.user.dto.request.AddressRequest
import com.numberone.daepiro.domain.user.dto.request.OnboardingRequest
import com.numberone.daepiro.domain.user.dto.request.UpdateGpsRequest
import com.numberone.daepiro.domain.user.dto.response.CheckNicknameResponse
import com.numberone.daepiro.domain.user.entity.UserEntity
import com.numberone.daepiro.domain.user.repository.UserRepository
import com.numberone.daepiro.domain.user.repository.findByIdOrThrow
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.exception.CustomErrorContext.INVALID_ADDRESS_FORMAT
import com.numberone.daepiro.global.exception.CustomErrorContext.INVALID_COORDINATES_CONVERTER
import com.numberone.daepiro.global.exception.CustomException
import com.numberone.daepiro.global.feign.KakaoAuthFeign
import com.numberone.daepiro.global.feign.KakaoLocalFeign
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val addressRepository: AddressRepository,
    private val userAddressRepository: UserAddressRepository,
    private val userDisasterTypeRepository: UserDisasterTypeRepository,
    private val disasterTypeRepository: DisasterTypeRepository,
    private val kakaoLocalFeign: KakaoLocalFeign,
    private val koreaLocationRepository: KoreaLocationRepository,
    @Value("\${kakao.client-id}") private val kakaoClientId: String,
) {
    fun checkNickname(
        nickname: String
    ): ApiResult<CheckNicknameResponse> {
        val user = userRepository.findByNickname(nickname)
        return ApiResult.ok(CheckNicknameResponse.from(user == null))
    }

    @Transactional
    fun setOnboardingData(
        request: OnboardingRequest,
        userId: Long
    ): ApiResult<Unit> {
        val user = userRepository.findByIdOrThrow(userId)
        user.initName(request.realname, request.nickname)
        handleOnboardingAddress(request.addresses, user)
        handleOnboardingDisasterType(request.disasterTypes, user)
        user.completeOnboarding()
        return ApiResult.ok()
    }

    private fun handleOnboardingDisasterType(
        disasterTypes: List<String>,
        user: UserEntity
    ) {
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
        user: UserEntity
    ) {
        val userAddressList = mutableListOf<UserAddress>()
        for (addressReq in addresses) {
            val addressInfo = AddressInfo.from(addressReq.address)
            val addressEntity = addressRepository.findByAddress(addressInfo.si, addressInfo.gu, addressInfo.dong)
                ?: addressRepository.save(Address.from(addressInfo))
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
        userId: Long
    ): ApiResult<Unit> {
        val location = getLocationFromGPS(request.longitude, request.latitude)
        val user = userRepository.findByIdOrThrow(userId)

        user.updateLocation(location)
        return ApiResult.ok()
    }

    private fun getLocationFromGPS(
        longitude: Double,
        latitude: Double
    ): KoreaLocation {
        val kakaoAddress = kakaoLocalFeign.getAddress(
            "KakaoAK $kakaoClientId",
            longitude,
            latitude
        ) ?: throw CustomException(INVALID_COORDINATES_CONVERTER)
        val address = kakaoAddress.documents.firstOrNull { it.regionType == "B" }
            ?: throw CustomException(INVALID_COORDINATES_CONVERTER)
        val location = koreaLocationRepository.findByAddressInfo(
            AddressInfo.from(
                address.siDo,
                if (address.siGunGu == "") null else address.siGunGu,
                if (address.eupMyeonDong == "") null else address.eupMyeonDong
            )
        ) ?: throw CustomException(INVALID_ADDRESS_FORMAT)

        return location
    }
}
