package com.numberone.daepiro.domain.sponsor.entity

import com.numberone.daepiro.domain.baseentity.PrimaryKeyEntity
import com.numberone.daepiro.domain.user.entity.UserEntity
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Cheering(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val user: UserEntity,

    var content: String
) : PrimaryKeyEntity() {
    companion object {
        fun of(
            user: UserEntity,
            content: String
        ): Cheering {
            return Cheering(
                user = user,
                content = content
            )
        }
    }

    fun updateContent(content: String) {
        this.content = content
    }
}
