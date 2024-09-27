package com.numberone.daepiro.domain.address.entity

import com.numberone.daepiro.domain.baseentity.PrimaryKeyEntity
import com.numberone.daepiro.domain.user.entity.UserEntity
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
@Tag(name = "`UserAddress`", description = "사용자 온보딩 주소 정보")
class UserAddress(
    val name: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val address: Address
) : PrimaryKeyEntity() {
    companion object {
        fun of(
            name: String,
            user: UserEntity,
            address: Address
        ): UserAddress {
            return UserAddress(
                name = name,
                user = user,
                address = address
            )
        }
    }
}
