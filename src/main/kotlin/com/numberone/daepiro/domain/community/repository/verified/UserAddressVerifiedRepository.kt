package com.numberone.daepiro.domain.community.repository.verified

import com.numberone.daepiro.domain.community.entity.UserAddressVerified
import org.springframework.data.jpa.repository.JpaRepository

interface UserAddressVerifiedRepository : JpaRepository<UserAddressVerified, Long> {
    fun findAllByUserId(userId: Long): List<UserAddressVerified>
    fun existsByUserIdAndAddressId(userId: Long, addressId: Long): Boolean
}
