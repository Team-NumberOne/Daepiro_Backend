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
    body: String = "",
    authUser: UserEntity? = null,
    parentComment: Comment? = null,
    documentType: CommentDocumentType,
    documentId: Long? = null,
    likeCount: Int = 0
) : PrimaryKeyEntity() {
    @Column(nullable = false)
    var body = body
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_user_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var authUser = authUser
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var parentComment = parentComment
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255)")
    var documentType = documentType
        protected set

    @Column(nullable = false)
    var documentId = documentId
        protected set

    @Column(nullable = false)
    var likeComment = likeCount
        protected set
}

enum class CommentDocumentType {
    ARTICLE, DISASTER
}
