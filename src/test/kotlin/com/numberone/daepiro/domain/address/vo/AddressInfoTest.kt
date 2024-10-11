package com.numberone.daepiro.domain.address.vo

import com.numberone.daepiro.domain.address.repository.KoreaLocationRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AddressInfoTest {
    @Autowired
    private lateinit var koreaLocationRepository: KoreaLocationRepository

    @Test
    fun `주소 변환 함수가 잘 작동하는 지 테스트한다`() {
        val locationList = koreaLocationRepository.findAll()
        for (location in locationList) {
            var address = (location.siDo ?: "") + " " + (location.siGunGu ?: "") + " " + (location.eupMyeonDong ?: "")
            address = address.replace("  ", " ").trim()
            val addressInfo = AddressInfo.from(address)
            // println("Location: $address")
            // println("AddressInfo: $addressInfo")
            assertEquals(location.siDo, addressInfo.si)
            assertEquals(location.siGunGu, addressInfo.gu)
            assertEquals(location.eupMyeonDong, addressInfo.dong)
        }
    }
}
