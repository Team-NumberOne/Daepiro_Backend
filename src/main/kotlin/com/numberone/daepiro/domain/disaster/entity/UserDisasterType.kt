package com.numberone.daepiro.domain.disaster.entity

import com.numberone.daepiro.domain.baseentity.PrimaryKeyEntity
import com.numberone.daepiro.domain.user.entity.UserEntity
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
@Tag(name = "`UserDisasterType`", description = "사용자 온보딩 재난유형 정보")
class UserDisasterType(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disaster_type_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val disasterType: DisasterType
) : PrimaryKeyEntity() {
    companion object {
        fun of(
            user: UserEntity,
            disasterType: DisasterType
        ): UserDisasterType {
            return UserDisasterType(
                user = user,
                disasterType = disasterType
            )
        }
    }
}
