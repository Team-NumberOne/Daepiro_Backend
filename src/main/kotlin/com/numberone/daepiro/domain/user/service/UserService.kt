package com.numberone.daepiro.domain.user.service

import com.numberone.daepiro.domain.address.entity.Address
import com.numberone.daepiro.domain.address.entity.UserAddress
import com.numberone.daepiro.domain.address.repository.AddressRepository
import com.numberone.daepiro.domain.address.repository.UserAddressRepository
import com.numberone.daepiro.domain.address.utils.AddressUtils
import com.numberone.daepiro.domain.disaster.entity.DisasterType
import com.numberone.daepiro.domain.disaster.entity.UserDisasterType
import com.numberone.daepiro.domain.disaster.repository.DisasterTypeRepository
import com.numberone.daepiro.domain.disaster.repository.UserDisasterTypeRepository
import com.numberone.daepiro.domain.disaster.repository.findByTypeOrThrow
import com.numberone.daepiro.domain.user.dto.request.OnboardingRequest
import com.numberone.daepiro.domain.user.dto.response.CheckNicknameResponse
import com.numberone.daepiro.domain.user.repository.UserRepository
import com.numberone.daepiro.domain.user.repository.findByIdOrThrow
import com.numberone.daepiro.global.dto.ApiResult
import com.numberone.daepiro.global.utils.SecurityContextUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val addressRepository: AddressRepository,
    private val userAddressRepository: UserAddressRepository,
    private val userDisasterTypeRepository: UserDisasterTypeRepository,
    private val disasterTypeRepository: DisasterTypeRepository
) {
    fun checkNickname(
        nickname: String
    ): ApiResult<CheckNicknameResponse> {
        val user = userRepository.findByNickname(nickname)
        return ApiResult.ok(CheckNicknameResponse.of(user == null))
    }

    @Transactional
    fun setOnboardingData(
        request: OnboardingRequest
    ): ApiResult<Unit> {
        val userId = SecurityContextUtils.getUserId()
        val user = userRepository.findByIdOrThrow(userId)

        user.initName(request.realname, request.nickname)
        for (addressReq in request.addresses) {
            val addressInfo = AddressUtils.getAddressInfo(addressReq.address)
            val addressEntity = addressRepository.findByAddress(addressInfo.si, addressInfo.gu, addressInfo.dong)
                ?: addressRepository.save(Address.of(addressInfo))
            userAddressRepository.save(
                UserAddress.of(
                    name = addressReq.name,
                    user = user,
                    address = addressEntity
                )
            )
        }
        for (str in request.disasterTypes) {
            val disasterType = disasterTypeRepository.findByTypeOrThrow(DisasterType.Type.kor2code(str))
            userDisasterTypeRepository.save(
                UserDisasterType.of(
                    user = user,
                    disasterType = disasterType
                )
            )
        }
        return ApiResult.ok()
    }
}
