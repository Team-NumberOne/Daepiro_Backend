package com.numberone.daepiro.domain.behaviourTip.entity

import com.numberone.daepiro.domain.baseentity.PrimaryKeyEntity
import com.numberone.daepiro.domain.disaster.entity.DisasterType
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "behaviour_tip")
class BehaviourTip(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disaster_type_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val disasterType: DisasterType,

    val filter: String,

    val tip: String
) : PrimaryKeyEntity()
