package com.numberone.daepiro.domain.community.dto.response

import com.numberone.daepiro.domain.community.entity.Comment

data class CommentSimpleResponse(val id: Long) {
    companion object {
        fun from(comment: Comment): CommentSimpleResponse {
            return CommentSimpleResponse(comment.id!!)
        }
    }
}
