package com.numberone.daepiro.domain.mypage.entity

import com.numberone.daepiro.domain.baseentity.PrimaryKeyEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Entity
class Reason(
    @Enumerated(EnumType.STRING)
    val type: ReasonType,
    val userId: Long,
) : PrimaryKeyEntity() {
    companion object {
        fun of(
            type: ReasonType,
            userId: Long
        ): Reason {
            return Reason(
                type = type,
                userId = userId
            )
        }
    }
}

enum class ReasonType {
    NEW, ALARM, INFO, USER, ETC
}
