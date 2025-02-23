package com.numberone.daepiro.domain.community.service

import com.numberone.daepiro.domain.community.dto.request.CreateCommentRequest
import com.numberone.daepiro.domain.community.dto.request.ReportRequest
import com.numberone.daepiro.domain.community.dto.response.CommentLikeResponse
import com.numberone.daepiro.domain.community.dto.response.CommentSimpleResponse
import com.numberone.daepiro.domain.community.entity.ArticleType
import com.numberone.daepiro.domain.community.entity.Comment
import com.numberone.daepiro.domain.community.entity.ReportedDocument
import com.numberone.daepiro.domain.community.repository.article.ArticleRepository
import com.numberone.daepiro.domain.community.repository.article.findByIdOrThrow
import com.numberone.daepiro.domain.community.repository.comment.CommentRepository
import com.numberone.daepiro.domain.community.repository.comment.ModifyCommentRequest
import com.numberone.daepiro.domain.community.repository.comment.findByIdOrThrow
import com.numberone.daepiro.domain.community.repository.reported.ReportedDocumentRepository
import com.numberone.daepiro.domain.community.repository.reported.isAlreadyReportedComment
import com.numberone.daepiro.domain.notification.entity.NotificationCategory
import com.numberone.daepiro.domain.notification.service.NotificationService
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
    private val notificationService: NotificationService
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
        if (parentComment != null) {
            notificationService.sendNotification(
                users = listOf(parentComment.authUser!!),
                title = "내 댓글에 ${author.nickname}님이 답글을 달았어요.",
                body = request.body,
                category = NotificationCategory.COMMUNITY
            )
        } else if (article.type == ArticleType.DONGNE) {
            notificationService.sendNotification(
                users = listOf(article.authUser!!),
                title = "내 게시글에 ${author.nickname}님이 댓글을 달았어요.",
                body = request.body,
                category = NotificationCategory.COMMUNITY
            )
        }

        return CommentSimpleResponse.from(comment)
    }

    @Transactional
    fun deleteComment(commentId: Long) {
        val comment = commentRepository.findByIdOrThrow(commentId)
            .also { it.delete() }
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
        val user = userRepository.findByIdOrThrow(userId)

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
                        user = user,
                        documentType = UserLikeDocumentType.COMMENT,
                        documentId = comment.id!!,
                    )
                )
                notificationService.sendNotification(
                    users = listOf(comment.authUser!!),
                    title = "${user.nickname}님이 내 댓글을 좋아해요.",
                    body = comment.body,
                    category = NotificationCategory.COMMUNITY
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
