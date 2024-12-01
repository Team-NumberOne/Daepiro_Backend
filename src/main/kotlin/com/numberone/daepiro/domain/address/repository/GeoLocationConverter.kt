package com.numberone.daepiro.domain.address.repository

import com.numberone.daepiro.domain.address.entity.Address
import com.numberone.daepiro.domain.address.vo.AddressInfo
import com.numberone.daepiro.global.exception.CustomErrorContext.INVALID_ADDRESS_FORMAT
import com.numberone.daepiro.global.exception.CustomErrorContext.INVALID_COORDINATES_CONVERTER
import com.numberone.daepiro.global.exception.CustomException
import com.numberone.daepiro.global.feign.KakaoLocalFeign
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class GeoLocationConverter(
    private val kakaoLocalFeign: KakaoLocalFeign,
    private val addressRepository: AddressRepository,
    @Value("\${kakao.client-id}") private val kakaoClientId: String,
) {
    fun findByLongitudeAndLatitudeOrThrow(longitude: Double, latitude: Double): Address {
        val kakaoAddress = kakaoLocalFeign.getAddress(
            "KakaoAK $kakaoClientId",
            longitude,
            latitude
        ) ?: throw CustomException(INVALID_COORDINATES_CONVERTER)
        val address = kakaoAddress.documents.firstOrNull { it.regionType == "B" }
            ?: throw CustomException(INVALID_COORDINATES_CONVERTER)
        val location = addressRepository.findByAddressInfo(
            AddressInfo.from(
                address.siDo,
                if (address.siGunGu == "") null else address.siGunGu,
                if (address.eupMyeonDong == "") null else address.eupMyeonDong
            )
        ) ?: throw CustomException(INVALID_ADDRESS_FORMAT)

        return location
    }

}
