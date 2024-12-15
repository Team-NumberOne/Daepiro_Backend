package com.numberone.daepiro.domain.community.service

import com.numberone.daepiro.domain.community.dto.request.CreateCommentRequest
import com.numberone.daepiro.domain.community.dto.request.ReportRequest
import com.numberone.daepiro.domain.community.dto.response.CommentLikeResponse
import com.numberone.daepiro.domain.community.dto.response.CommentSimpleResponse
import com.numberone.daepiro.domain.community.entity.Comment
import com.numberone.daepiro.domain.community.entity.ReportedDocument
import com.numberone.daepiro.domain.community.repository.article.ArticleRepository
import com.numberone.daepiro.domain.community.repository.article.findByIdOrThrow
import com.numberone.daepiro.domain.community.repository.comment.CommentRepository
import com.numberone.daepiro.domain.community.repository.comment.ModifyCommentRequest
import com.numberone.daepiro.domain.community.repository.comment.findByIdOrThrow
import com.numberone.daepiro.domain.community.repository.reported.ReportedDocumentRepository
import com.numberone.daepiro.domain.community.repository.reported.isAlreadyReportedComment
import com.numberone.daepiro.domain.user.entity.UserLike
import com.numberone.daepiro.domain.user.entity.UserLikeDocumentType
import com.numberone.daepiro.domain.user.repository.UserLikeRepository
import com.numberone.daepiro.domain.user.repository.UserRepository
import com.numberone.daepiro.domain.user.repository.findByIdOrThrow
import com.numberone.daepiro.domain.user.repository.isLikedComment
import com.numberone.daepiro.global.exception.CustomErrorContext
import com.numberone.daepiro.global.exception.CustomException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val articleRepository: ArticleRepository,
    private val userRepository: UserRepository,
    private val userLikeRepository: UserLikeRepository,
    private val reportedDocumentRepository: ReportedDocumentRepository,
) {
    @Transactional
    fun createComment(userId: Long, request: CreateCommentRequest): CommentSimpleResponse {
        val author = userRepository.findByIdOrThrow(userId)
        val parentComment = request.parentCommentId?.let { commentRepository.findByIdOrThrow(it) }
        val article = articleRepository.findByIdOrThrow(request.articleId)

        val comment = commentRepository.save(
            Comment.of(
                body = request.body,
                authUser = author,
                article = article,
                parentComment = parentComment,
            )
        )

        return CommentSimpleResponse.from(comment)
    }

    @Transactional
    fun deleteComment(commentId: Long) {
        val comment = commentRepository.findByIdOrThrow(commentId)
            .also { commentRepository.delete(it) }
        articleRepository.findByIdOrNull(comment.documentId)
            ?.also { it.decreaseCommentCount() }
    }

    @Transactional
    fun modifyComment(commentId: Long, request: ModifyCommentRequest): CommentSimpleResponse {
        val comment = commentRepository.findByIdOrThrow(commentId)
        return CommentSimpleResponse.from(
            comment = comment.modifyComment(request.body)
        )
    }

    @Transactional
    fun like(commentId: Long, userId: Long): CommentLikeResponse {
        val comment = commentRepository.findByIdOrThrow(commentId)

        when (userLikeRepository.isLikedComment(userId, comment)) {
            true -> {
                comment.decreaseLikeCount()
                userLikeRepository.deleteByUserIdAndDocumentTypeAndDocumentId(
                    userId = userId,
                    documentType = UserLikeDocumentType.COMMENT,
                    documentId = comment.id!!,
                )
            }

            false -> {
                comment.increaseLikeCount()
                userLikeRepository.save(
                    UserLike(
                        user = userRepository.findByIdOrThrow(userId),
                        documentType = UserLikeDocumentType.COMMENT,
                        documentId = comment.id!!,
                    )
                )
            }
        }

        return CommentLikeResponse.from(comment)
    }

    @Transactional
    fun report(
        userId: Long,
        commentId: Long,
        request: ReportRequest,
    ) {
        val user = userRepository.findByIdOrThrow(userId)
        val comment = commentRepository.findByIdOrThrow(commentId)

        if (reportedDocumentRepository.isAlreadyReportedComment(user, comment)) {
            throw CustomException(CustomErrorContext.INVALID_VALUE, "해당 유저에 의해 이미 신고된 댓글입니다.")
        }

        reportedDocumentRepository.save(
            ReportedDocument.from(
                comment = comment,
                user = user,
                type = request.type,
                detail = request.detail,
                email = request.email,
            )
        )
        commentRepository.save(
            comment.increaseReportCount()
        )
    }
}
