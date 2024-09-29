package com.numberone.daepiro.domain.address.repository

import com.numberone.daepiro.domain.address.entity.UserAddress
import org.springframework.data.jpa.repository.JpaRepository

interface UserAddressRepository : JpaRepository<UserAddress, Long>
