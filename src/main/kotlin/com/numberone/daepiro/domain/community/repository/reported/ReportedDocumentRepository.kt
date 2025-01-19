package com.numberone.daepiro.domain.community.repository.reported

import com.numberone.daepiro.domain.community.entity.Article
import com.numberone.daepiro.domain.community.entity.Comment
import com.numberone.daepiro.domain.community.entity.ReportedDocument
import com.numberone.daepiro.domain.community.entity.ReportedDocumentType
import com.numberone.daepiro.domain.sponsor.entity.Cheering
import com.numberone.daepiro.domain.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ReportedDocumentRepository : JpaRepository<ReportedDocument, Long> {
    fun existsByReportUserIdAndDocumentIdAndDocumentType(
        userId: Long,
        documentId: Long,
        documentType: ReportedDocumentType
    ): Boolean
}

fun ReportedDocumentRepository.isAlreadyReportedArticle(
    user: UserEntity,
    article: Article,
): Boolean {
    return existsByReportUserIdAndDocumentIdAndDocumentType(
        userId = user.id!!,
        documentType = ReportedDocumentType.ARTICLE,
        documentId = article.id!!
    )
}

fun ReportedDocumentRepository.isAlreadyReportedComment(
    user: UserEntity,
    comment: Comment,
): Boolean {
    return existsByReportUserIdAndDocumentIdAndDocumentType(
        userId = user.id!!,
        documentType = ReportedDocumentType.COMMENT,
        documentId = comment.id!!
    )
}

fun ReportedDocumentRepository.isAlreadyReportedCheering(
    user: UserEntity,
    cheering: Cheering,
): Boolean {
    return existsByReportUserIdAndDocumentIdAndDocumentType(
        userId = user.id!!,
        documentType = ReportedDocumentType.CHEERING,
        documentId = cheering.id!!
    )
}
