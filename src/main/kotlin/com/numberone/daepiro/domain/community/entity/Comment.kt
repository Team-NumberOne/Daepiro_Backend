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
    documentType: ArticleType? = null,
    documentId: Long? = null,
    likeCount: Int = 0
) : PrimaryKeyEntity() {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_user_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var authUser: UserEntity? = authUser
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var parentComment: Comment? = parentComment
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255)")
    var documentType: ArticleType? = documentType
        protected set

    @Column(nullable = false)
    var documentId: Long? = documentId
        protected set

    @Column(nullable = false)
    var body: String = body
        protected set

    @Column(nullable = false)
    var likeCount: Int = likeCount
        protected set

    fun modifyComment(body: String): Comment {
        return this.apply {
            this.body = body
        }
    }

    fun increaseLikeCount(): Comment {
        return this.apply {
            likeCount++;
        }
    }

    fun decreaseLikeCount(): Comment {
        return this.apply {
            likeCount = (likeCount - 1)
                .coerceAtLeast(0)
        }
    }

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
