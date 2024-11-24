package com.numberone.daepiro.domain.file.entity

import com.numberone.daepiro.domain.baseentity.PrimaryKeyEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Deprecated("Deprecated. File 관련 엔드포인트를 분리하지 않는다.")
@Entity
@Table(name = "`file`")
class FileEntity(
    @Column(nullable = false)
    val path: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255)")
    val documentType: FileDocumentType,

    @Column(nullable = false)
    val documentId: Long
) : PrimaryKeyEntity() {
    companion object {
        fun of(
            path: String,
            documentType: FileDocumentType,
            documentId: Long
        ): FileEntity {
            return FileEntity(
                path = path,
                documentType = documentType,
                documentId = documentId,
            )
        }
    }
}

enum class FileDocumentType {
    ARTICLE, DONATION, USER_PROFILE
}
