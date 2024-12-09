package com.numberone.daepiro.domain.community.service

import com.numberone.daepiro.domain.community.dto.request.CreateCommentRequest
import com.numberone.daepiro.domain.community.dto.response.CommentSimpleResponse
import com.numberone.daepiro.domain.community.entity.Comment
import com.numberone.daepiro.domain.community.repository.article.ArticleRepository
import com.numberone.daepiro.domain.community.repository.article.findByIdOrThrow
import com.numberone.daepiro.domain.community.repository.comment.CommentRepository
import com.numberone.daepiro.domain.community.repository.comment.ModifyCommentRequest
import com.numberone.daepiro.domain.community.repository.comment.findByIdOrThrow
import com.numberone.daepiro.domain.user.repository.UserRepository
import com.numberone.daepiro.domain.user.repository.findByIdOrThrow
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val articleRepository: ArticleRepository,
    private val userRepository: UserRepository,
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
}
