package com.numberone.daepiro.domain.disaster.entity

import com.numberone.daepiro.domain.address.entity.KoreaLocation
import com.numberone.daepiro.domain.baseentity.PrimaryKeyEntity
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "`disaster`")
class Disaster(
    val generatedAt: LocalDateTime,

    val messageId: Long,

    val message: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val location: KoreaLocation,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disaster_type_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val disasterType: DisasterType,

    val isDummy: Boolean
) : PrimaryKeyEntity() {
    companion object {
        fun of(
            generatedAt: LocalDateTime,
            messageId: Long,
            message: String,
            location: KoreaLocation,
            disasterType: DisasterType
        ): Disaster {
            return Disaster(
                generatedAt = generatedAt,
                messageId = messageId,
                message = message,
                location = location,
                disasterType = disasterType,
                isDummy = false
            )
        }

        fun ofDummy(
            generatedAt: LocalDateTime,
            messageId: Long,
            message: String,
            location: KoreaLocation,
            disasterType: DisasterType
        ): Disaster {
            return Disaster(
                generatedAt = generatedAt,
                messageId = messageId,
                message = message,
                location = location,
                disasterType = disasterType,
                isDummy = true
            )
        }
    }
}
