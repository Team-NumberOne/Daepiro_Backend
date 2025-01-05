package com.numberone.daepiro.domain.community.service

import com.numberone.daepiro.domain.address.entity.Address
import com.numberone.daepiro.domain.address.repository.AddressRepository
import com.numberone.daepiro.domain.address.repository.GeoLocationConverter
import com.numberone.daepiro.domain.address.repository.findAllRelatedAddressBy
import com.numberone.daepiro.domain.address.repository.findByAddressInfoOrThrow
import com.numberone.daepiro.domain.address.repository.findByIdOrThrow
import com.numberone.daepiro.domain.address.vo.AddressInfo
import com.numberone.daepiro.domain.community.dto.request.UserAddressVerifiedRequest
import com.numberone.daepiro.domain.community.dto.response.UserAddressVerifiedResponse
import com.numberone.daepiro.domain.community.entity.UserAddressVerified
import com.numberone.daepiro.domain.community.repository.verified.UserAddressVerifiedRepository
import com.numberone.daepiro.domain.user.entity.UserEntity
import com.numberone.daepiro.domain.user.repository.UserRepository
import com.numberone.daepiro.domain.user.repository.findByIdOrThrow
import com.numberone.daepiro.global.exception.CustomErrorContext
import com.numberone.daepiro.global.exception.CustomException
import com.numberone.daepiro.global.utils.TransactionUtils
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserAddressVerifiedService(
    private val geoLocationConverter: GeoLocationConverter,
    private val addressRepository: AddressRepository,
    private val userVerifiedRepository: UserAddressVerifiedRepository,
    private val userRepository: UserRepository,
) {
    private val log = KotlinLogging.logger { }

    @Transactional(readOnly = true)
    fun getVerified(userId: Long): List<UserAddressVerifiedResponse> {
        val user = userRepository.findByIdOrThrow(userId)

        // 1. user 가 온보딩때 선택했던 주소 collect
        val onboardingAddress =
            addressRepository.findAllByIdIn(user.userAddresses.mapNotNull { it.address.id }.distinct())

        // 2. 해당 user 가 인증받았던 이력 조회
        val verified = userVerifiedRepository.findAllByUserId(user.id!!)
        val verifiedMapOfAddressId = verified.associate { it.addressId to it.verified }

        return onboardingAddress.map {
            UserAddressVerifiedResponse.of(
                address = it,
                // 3. 인증받았던 주소 내에 속하면 인증받은 것임.
                isVerified = verifiedMapOfAddressId[it.id] ?: false
            )
        }
    }

    fun getVerifiedOne(userId: Long, addressId: Long): Boolean {
        val verified = userVerifiedRepository.findByUserIdAndAddressId(userId, addressId)

        return verified?.verified ?: false
    }

    fun verify(
        userId: Long,
        request: UserAddressVerifiedRequest
    ) {
        val userEntity = userRepository.findByIdOrThrow(userId)
        // 1. 요청받은 address 에 대해서 현재 주소와 일치하는지 검사한다.

        // ㄴ 1.1 위경도 기반으로 Address 를 collect
        val addressByGeoLocation = addressRepository.findAllRelatedAddressBy(
            address = geoLocationConverter.findByLongitudeAndLatitudeOrThrow(
                request.longitude,
                request.latitude,
            )
        )

        // ㄴ 1.2 인증받고자 하는 Address 를 collect
        val addressToVerify = addressRepository.findByAddressInfoOrThrow(AddressInfo.from(request.address))

        // 2. 인증받고자 하는 주소(1.2) 가 위경도 기반의 주소 (1.1) 에 속하는지 검사한다. 속하는지 여부는 address Id 로 판단한다.
        val addressIdByGeoLocationMap = addressByGeoLocation.map { it.id }.toSet()

        when (addressToVerify.id in addressIdByGeoLocationMap) {
            true -> {
                success(
                    userId = userId,
                    addressToVerify = addressToVerify,
                    userEntity = userEntity
                )
            }

            false -> {
                failed(
                    addressByGeoLocation = addressByGeoLocation,
                    addressToVerify = addressToVerify,
                    request = request
                )
            }
        }
    }

    private fun success(
        userId: Long,
        addressToVerify: Address,
        userEntity: UserEntity
    ) {
        TransactionUtils.writable {

            val isExist = userVerifiedRepository.existsByUserIdAndAddressId(
                userId = userId,
                addressId = addressToVerify.id!!,
            )

            if (isExist) {
                return@writable
            }

            userVerifiedRepository.save(
                UserAddressVerified(
                    verified = true,
                    userEntity = userEntity,
                    address = addressToVerify
                )
            )
        }
    }

    private fun failed(
        addressByGeoLocation: Set<Address>,
        addressToVerify: Address,
        request: UserAddressVerifiedRequest
    ): Nothing {
        val currentLocationAddresses = addressByGeoLocation.map { it.toFullAddress() }

        val message = """
               동네 인증 실패 원인 상세:
             - 사용자의 현 위치: (latitude: ${request.latitude}, longitude: ${request.longitude})
             - 현 위치 기반 주소:
                 ${currentLocationAddresses.joinToString(separator = ",")}
             - 인증받고자 하는 주소:
                 ${addressToVerify.toFullAddress()}
             : 현 위치 기반 주소 내에 인증받고자 하는 주소가 포함되어야 성공 처리 됩니다.
             """.trimIndent()

        log.debug {
            message
        }

        throw CustomException(context = CustomErrorContext.DONGNE_VERIFIED_IS_FAILED, additionalDetail = message)
    }
}

