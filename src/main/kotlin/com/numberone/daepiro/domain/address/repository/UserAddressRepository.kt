package com.numberone.daepiro.domain.address.repository

import com.numberone.daepiro.domain.address.entity.UserAddress
import com.numberone.daepiro.domain.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserAddressRepository : JpaRepository<UserAddress, Long> {
    fun deleteAllByUser(user: UserEntity)
}
