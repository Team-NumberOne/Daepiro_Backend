package com.numberone.daepiro.domain.disaster.repository

import com.numberone.daepiro.domain.disaster.entity.UserDisasterType
import org.springframework.data.jpa.repository.JpaRepository

interface UserDisasterTypeRepository :JpaRepository<UserDisasterType, Long>
