package com.numberone.daepiro.domain.address.repository

import com.numberone.daepiro.domain.address.entity.UserAddress
import com.numberone.daepiro.domain.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional

interface UserAddressRepository : JpaRepository<UserAddress, Long> {
    fun findByAddressIdIn(addressId: List<Long>): List<UserAddress>

    @Modifying
    @Transactional
    @Query("DELETE FROM UserAddress ua WHERE ua.user = :user")
    fun deleteAllByUser(user: UserEntity)
}
