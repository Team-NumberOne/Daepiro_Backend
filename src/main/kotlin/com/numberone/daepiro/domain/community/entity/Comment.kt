package com.numberone.daepiro.domain.community.entity

import com.numberone.daepiro.domain.baseentity.PrimaryKeyEntity
import com.numberone.daepiro.domain.user.entity.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "`comment`")
class Comment(
    @Column(nullable = false)
    val body: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_user_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val authUser: UserEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    val parentComment: Comment? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255)")
    val documentType: CommentDocumentType,

    @Column(nullable = false)
    val documentId: Long? = null,

    @Column(nullable = false)
    val likeCount: Int = 0
) : PrimaryKeyEntity()

enum class CommentDocumentType {
    ARTICLE, DISASTER
}
