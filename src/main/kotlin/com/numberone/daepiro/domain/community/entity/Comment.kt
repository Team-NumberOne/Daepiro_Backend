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
    val documentType: ArticleType? = null,

    @Column(nullable = false)
    val documentId: Long? = null,

    @Column(nullable = false)
    val likeCount: Int = 0
) : PrimaryKeyEntity() {
    companion object {
        fun of(
            body: String,
            authUser: UserEntity,
            parentComment: Comment? = null,
            article: Article,
        ): Comment {
            article.increaseCommentCount()
            return Comment(
                body = body,
                authUser = authUser,
                parentComment = parentComment,
                documentType = article.type,
                documentId = article.id,
                likeCount = 0,
            )
        }
    }
}
