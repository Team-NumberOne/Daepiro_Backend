package com.numberone.daepiro.domain.address.utils

import com.numberone.daepiro.domain.address.entity.Address
import com.numberone.daepiro.global.exception.CustomErrorContext
import com.numberone.daepiro.global.exception.CustomException

object AddressUtils {
    private val suffix = arrayOf(
        listOf("시", "도"),
        listOf("시", "군", "구"),
        listOf("읍", "면", "동")
    )

    fun getAddressInfo(
        fullAddress: String
    ): AddressInfo {
        val words = fullAddress.split(" ")
        var depth = 0
        var i = 0
        val address = arrayOfNulls<String>(3)

        if (words.size > 3)
            throw CustomException(CustomErrorContext.INVALID_ADDRESS_FORMAT)
        while (i < words.size) {
            while (!(suffix[depth].any { suffix -> words[i].endsWith(suffix) })) {
                depth++
                if (depth > 2)
                    throw CustomException(CustomErrorContext.INVALID_ADDRESS_FORMAT)
            }
            address[depth++] = words[i++]
        }
        val si = address[0] ?: throw CustomException(CustomErrorContext.INVALID_ADDRESS_FORMAT)
        return AddressInfo(
            si = si,
            gu = address[1],
            dong = address[2],
            depth = depth
        )
    }

    data class AddressInfo(
        val si: String,
        val gu: String? = null,
        val dong: String? = null,
        val depth: Int = 0
    )
}
