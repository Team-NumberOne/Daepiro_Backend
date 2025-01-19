package com.numberone.daepiro.domain.community.repository.comment

import com.numberone.daepiro.domain.community.dto.response.CommentResponse
import com.numberone.daepiro.domain.community.dto.response.QAuthorResponse
import com.numberone.daepiro.domain.community.dto.response.QCommentResponse
import com.numberone.daepiro.domain.community.entity.QComment
import com.numberone.daepiro.domain.community.entity.QComment.comment
import com.numberone.daepiro.domain.user.entity.QUserEntity
import com.querydsl.jpa.impl.JPAQueryFactory

class CommentRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : CommentRepositoryCustom {
    override fun findCommentsByDocumentId(articleId: Long,userId:Long): List<CommentResponse> {
        val parentComment = QComment("parentComment")
        val author = QUserEntity("author")

        return queryFactory.select(
            QCommentResponse(
                comment.id,
                comment.body,
                QAuthorResponse(
                    author.id,
                    author.nickname,
                    author.realname,
                    author.isCompletedOnboarding,
                    author.profileImageUrl
                ),
                comment.likeCount,
                parentComment.id,
                comment.createdAt,
                comment.lastModifiedAt,
                comment.deletedAt,
                comment.authUser.id.eq(userId),
            )
        ).from(comment)
            .leftJoin(comment.authUser, author).on(comment.authUser.id.eq(author.id))
            .leftJoin(comment.parentComment, parentComment).on(comment.parentComment.id.eq(parentComment.id))
            .where(comment.documentId.eq(articleId))
            .orderBy(comment.id.desc())
            .fetch()
    }
}
