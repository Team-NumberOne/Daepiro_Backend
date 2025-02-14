package com.numberone.daepiro.domain.mypage.repository

import com.numberone.daepiro.domain.mypage.entity.Reason
import org.springframework.data.jpa.repository.JpaRepository

interface ReasonRepository:JpaRepository<Reason, Long>{
}
