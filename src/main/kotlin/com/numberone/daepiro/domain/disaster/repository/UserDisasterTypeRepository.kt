package com.numberone.daepiro.domain.disaster.repository

import com.numberone.daepiro.domain.disaster.entity.UserDisasterType
import com.numberone.daepiro.domain.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserDisasterTypeRepository : JpaRepository<UserDisasterType, Long> {
    fun deleteAllByUser(user: UserEntity)
}
