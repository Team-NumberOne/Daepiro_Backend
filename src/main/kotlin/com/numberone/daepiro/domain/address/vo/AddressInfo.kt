package com.numberone.daepiro.domain.address.vo

import com.numberone.daepiro.global.exception.CustomErrorContext
import com.numberone.daepiro.global.exception.CustomException

data class AddressInfo(
    val si: String,
    val gu: String? = null,
    val dong: String? = null,
    val depth: Int = 0
) {
    companion object {
        private val suffix = arrayOf(
            listOf("시", "도"),
            listOf("시", "군", "구"),
            listOf("읍", "면", "동", "가", "로")
        )

        fun from(
            fullAddress: String
        ): AddressInfo {
            val tempString = "TEMP_STRING"
            val pattern = Regex("(청주시|천안시|수원시|성남시|안양시|안산시|고양시|포항시|용인시|창원시|전주시|부천시) \\S*구")
            val matchResult = pattern.find(fullAddress)
            val matchedString = matchResult?.value ?: ""
            val fullAddressModified = fullAddress.replace(matchedString, tempString)
            val words = fullAddressModified.split(" ").map { it.replace(tempString, matchedString) }

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
    }
}
