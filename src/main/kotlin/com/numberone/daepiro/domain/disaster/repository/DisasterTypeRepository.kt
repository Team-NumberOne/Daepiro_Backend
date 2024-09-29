package com.numberone.daepiro.domain.disaster.repository

import com.numberone.daepiro.domain.disaster.entity.DisasterType
import com.numberone.daepiro.domain.disaster.entity.DisasterType.DisasterValue
import com.numberone.daepiro.global.exception.CustomErrorContext.NOT_FOUND_DISASTER_TYPE
import com.numberone.daepiro.global.exception.CustomException
import org.springframework.data.jpa.repository.JpaRepository

interface DisasterTypeRepository : JpaRepository<DisasterType, Long> {
    fun findByType(type: DisasterValue): DisasterType?
}

fun DisasterTypeRepository.findByTypeOrThrow(type: DisasterValue): DisasterType {
    return this.findByType(type) ?: throw CustomException(NOT_FOUND_DISASTER_TYPE)
}
