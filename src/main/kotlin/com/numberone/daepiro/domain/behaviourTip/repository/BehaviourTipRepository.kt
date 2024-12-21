package com.numberone.daepiro.domain.behaviourTip.repository

import com.numberone.daepiro.domain.behaviourTip.entity.BehaviourTip
import org.springframework.data.jpa.repository.JpaRepository

interface BehaviourTipRepository: JpaRepository<BehaviourTip, Long> {
}
