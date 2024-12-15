package com.numberone.daepiro.domain.community.entity

import com.numberone.daepiro.domain.baseentity.PrimaryKeyEntity
import com.numberone.daepiro.domain.user.entity.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "reported_document")
class ReportedDocument(
    documentId: Long,
    documentType: ReportedDocumentType,
    reportUser: UserEntity,
    type: String,
    detail: String,
    email: String,
) : PrimaryKeyEntity() {
    @Column(nullable = false)
    var documentId: Long = documentId
        protected set

    @Column(nullable = false)
    var documentType: ReportedDocumentType = documentType
        protected set

    @Column(nullable = false)
    var reportUserId: Long = reportUser.id!!
        protected set

    // 신고 유형
    @Column(nullable = false)
    var type: String = type
        protected set

    // 상세 이유
    @Column(nullable = false)
    var detail: String = detail
        protected set

    // 이메일
    @Column(nullable = false)
    var email: String = email
        protected set

    companion object {
        fun from(article: Article, user: UserEntity, type: String, detail: String, email: String): ReportedDocument {
            return ReportedDocument(
                documentId = article.id!!,
                documentType = ReportedDocumentType.ARTICLE,
                reportUser = user,
                type = type,
                detail = detail,
                email = email
            )
        }

        fun from(comment: Comment, user: UserEntity, type: String, detail: String, email: String): ReportedDocument {
            return ReportedDocument(
                documentId = comment.id!!,
                documentType = ReportedDocumentType.COMMENT,
                reportUser = user,
                type = type,
                detail = detail,
                email = email
            )
        }
    }
}

enum class ReportedDocumentType {
    ARTICLE,
    COMMENT
}
