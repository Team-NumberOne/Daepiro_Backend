package com.numberone.daepiro.domain.disaster.entity

import com.numberone.daepiro.domain.baseentity.PrimaryKeyEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "`disaster`")
class Disaster(
    val generatedAt: LocalDateTime,

    val messageId: Long,

    val message: String,

    val locationId: Long,

    val isDummy: Boolean
) : PrimaryKeyEntity() {
    companion object {
        fun of(
            generatedAt: LocalDateTime,
            messageId: Long,
            message: String,
            locationId: Long
        ): Disaster {
            return Disaster(
                generatedAt = generatedAt,
                messageId = messageId,
                message = message,
                locationId = locationId,
                isDummy = false
            )
        }

        fun ofDummy(
            generatedAt: LocalDateTime,
            messageId: Long,
            message: String,
            locationId: Long
        ): Disaster {
            return Disaster(
                generatedAt = generatedAt,
                messageId = messageId,
                message = message,
                locationId = locationId,
                isDummy = true
            )
        }
    }
}
