package com.numberone.daepiro.domain.address.repository

import com.numberone.daepiro.domain.address.entity.KoreaLocation
import org.springframework.data.jpa.repository.JpaRepository

interface KoreaLocationRepository : JpaRepository<KoreaLocation, Long>
