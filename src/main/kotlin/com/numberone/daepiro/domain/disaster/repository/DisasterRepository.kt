package com.numberone.daepiro.domain.disaster.repository

import com.numberone.daepiro.domain.disaster.entity.Disaster
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface DisasterRepository : JpaRepository<Disaster, Long> {
    @Query("select d from Disaster d order by d.messageId desc")
    fun findLatestDisaster(): List<Disaster>

    fun findByLocationIdIn(locationIds: List<Long>): List<Disaster>
}
