package com.numberone.daepiro.domain.user.entity

import com.numberone.daepiro.domain.baseentity.PrimaryKeyEntity
import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "`user_like`")
class UserLike(
    user: UserEntity?,
    documentType: UserLikeDocumentType,
    documentId: Long
) : PrimaryKeyEntity() {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var user = user
        protected set
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var documentType = documentType
        protected set

    @Column(nullable = false)
    var documentId = documentId
        protected set
}

enum class UserLikeDocumentType {
    ARTICLE, DISASTER, COMMENT
}
