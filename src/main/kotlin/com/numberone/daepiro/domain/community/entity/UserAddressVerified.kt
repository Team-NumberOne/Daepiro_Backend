package com.numberone.daepiro.domain.community.entity

import com.numberone.daepiro.domain.address.entity.Address
import com.numberone.daepiro.domain.baseentity.PrimaryKeyEntity
import com.numberone.daepiro.domain.user.entity.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "user_address_verified")
class UserAddressVerified(
    verified: Boolean = false,
    userEntity: UserEntity,
    address: Address,
) : PrimaryKeyEntity() {
    @Column(nullable = false)
    var verified: Boolean = verified
        protected set

    @Column(nullable = false)
    var userId: Long = userEntity.id!!
        protected set

    @Column(nullable = false)
    var addressId: Long = address.id!!
        protected set
}
