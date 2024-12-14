package com.numberone.daepiro.domain.community.dto.response

import com.numberone.daepiro.domain.community.entity.Comment

data class CommentSimpleResponse(val id: Long) {
    companion object {
        fun from(comment: Comment): CommentSimpleResponse {
            return CommentSimpleResponse(comment.id!!)
        }
    }
}

data class CommentLikeResponse(
    val id: Long,
    val likeCount: Int,
) {
    companion object {
        fun from(comment: Comment): CommentLikeResponse {
            return CommentLikeResponse(comment.id!!, comment.likeCount)
        }
    }
}
