package com.numberone.daepiro.domain.user.repository

import com.numberone.daepiro.domain.community.entity.Article
import com.numberone.daepiro.domain.community.entity.Comment
import com.numberone.daepiro.domain.user.entity.UserLike
import com.numberone.daepiro.domain.user.entity.UserLikeDocumentType
import org.springframework.data.jpa.repository.JpaRepository

interface UserLikeRepository : JpaRepository<UserLike, Long> {
    fun existsByUserIdAndDocumentTypeAndDocumentId(
        userId: Long,
        documentType: UserLikeDocumentType,
        documentId: Long,
    ): Boolean

    fun deleteByUserIdAndDocumentTypeAndDocumentId(
        userId: Long,
        documentType: UserLikeDocumentType,
        documentId: Long,
    )
}

fun UserLikeRepository.isLikedArticle(
    userId: Long,
    article: Article,
): Boolean {
    return existsByUserIdAndDocumentTypeAndDocumentId(
        userId = userId,
        documentType = UserLikeDocumentType.from(article.type),
        documentId = article.id!!,
    )
}

fun UserLikeRepository.isLikedComment(
    userId: Long,
    comment: Comment,
): Boolean {
    return existsByUserIdAndDocumentTypeAndDocumentId(
        userId = userId,
        documentType = UserLikeDocumentType.COMMENT,
        documentId = comment.id!!,
    )
}
