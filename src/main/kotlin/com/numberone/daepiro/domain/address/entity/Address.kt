package com.numberone.daepiro.domain.address.entity

import com.numberone.daepiro.domain.address.utils.AddressUtils
import com.numberone.daepiro.domain.baseentity.PrimaryKeyEntity
import com.numberone.daepiro.global.exception.CustomErrorContext.INVALID_ADDRESS_FORMAT
import com.numberone.daepiro.global.exception.CustomException
import jakarta.persistence.*
import org.hibernate.annotations.Comment

@Entity
@Table(name = "`address`")
class Address(
    @Comment("시/도")
    val si: String,

    @Comment("시/군/구")
    val gu: String? = null,

    @Comment("읍/면/동")
    val dong: String? = null,

    // 1: 시/도, 2: 시/군/구, 3: 읍/면/동
    val depth: Int = 0,

    @OneToMany(mappedBy = "address", cascade = [CascadeType.ALL])
    val userAddresses: List<UserAddress> = emptyList()
) : PrimaryKeyEntity() {
    companion object {
        fun of(
            info: AddressUtils.AddressInfo,
        ): Address {
            return Address(
                si = info.si,
                gu = info.gu,
                dong = info.dong,
                depth = info.depth
            )
        }
    }
}
