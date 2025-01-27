package com.numberone.daepiro.domain.community.repository.comment

import com.numberone.daepiro.domain.community.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CommentRepository : JpaRepository<Comment, Long>, CommentRepositoryCustom {
    @Query("SELECT c FROM Comment c WHERE c.documentId = :documentId AND c.deletedAt IS NULL ORDER BY c.likeCount DESC LIMIT 2")
    fun findPopularComments(documentId: Long): List<Comment>

    @Query("SELECT c FROM Comment c WHERE c.documentId = :documentId AND c.parentComment.id IS NULL ORDER BY c.createdAt ASC")
    fun findParentComments(documentId: Long): List<Comment>

    @Query("SELECT c FROM Comment c WHERE c.parentComment.id = :parentCommentId ORDER BY c.createdAt ASC")
    fun findChildComments(parentCommentId: Long): List<Comment>
}

fun CommentRepository.findByIdOrThrow(id: Long): Comment {
    return findById(id).orElseThrow { IllegalArgumentException("Comment with id $id not found") }
}
