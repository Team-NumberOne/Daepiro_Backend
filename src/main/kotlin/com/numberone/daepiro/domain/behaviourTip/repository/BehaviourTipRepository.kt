package com.numberone.daepiro.domain.behaviourTip.repository

import com.numberone.daepiro.domain.behaviourTip.entity.BehaviourTip
import com.numberone.daepiro.domain.disaster.entity.DisasterType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BehaviourTipRepository : JpaRepository<BehaviourTip, Long> {

    @Query("select distinct t.filter from BehaviourTip t where t.disasterType.id=:disasterTypeId")
    fun findTipFilters(disasterTypeId: Long): List<String>

    fun findByDisasterTypeAndFilter(disasterType: DisasterType, filter: String): List<BehaviourTip>
}
