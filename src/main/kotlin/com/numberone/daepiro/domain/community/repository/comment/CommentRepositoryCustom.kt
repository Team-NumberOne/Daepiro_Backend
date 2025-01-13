package com.numberone.daepiro.domain.community.repository.comment

import com.numberone.daepiro.domain.community.dto.response.CommentResponse

interface CommentRepositoryCustom {
    fun findCommentsByDocumentId(articleId: Long, userId: Long): List<CommentResponse>
}
