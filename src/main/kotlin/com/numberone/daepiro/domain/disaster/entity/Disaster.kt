package com.numberone.daepiro.domain.disaster.entity

import com.numberone.daepiro.domain.address.entity.Address
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
    @JoinColumn(name = "address_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val address: Address,

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
            address: Address,
            disasterType: DisasterType
        ): Disaster {
            return Disaster(
                generatedAt = generatedAt,
                messageId = messageId,
                message = message,
                address = address,
                disasterType = disasterType,
                isDummy = false
            )
        }

        fun ofDummy(
            generatedAt: LocalDateTime,
            messageId: Long,
            message: String,
            address: Address,
            disasterType: DisasterType
        ): Disaster {
            return Disaster(
                generatedAt = generatedAt,
                messageId = messageId,
                message = message,
                address = address,
                disasterType = disasterType,
                isDummy = true
            )
        }
    }

    fun getTitle(): String {
        return "${address.toFullAddress()} ${disasterType.type.korean} 발생"
    }
}
