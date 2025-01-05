package com.numberone.daepiro.domain.sponsor.repository

import com.numberone.daepiro.domain.community.entity.Article
import com.numberone.daepiro.domain.sponsor.entity.Cheering
import org.springframework.data.jpa.repository.JpaRepository

interface CheeringRepository: JpaRepository<Cheering, Long>{
    fun findTop100ByOrderByCreatedAtDesc(): List<Cheering>
    fun findTop20ByOrderByCreatedAtDesc(): List<Cheering>
}
