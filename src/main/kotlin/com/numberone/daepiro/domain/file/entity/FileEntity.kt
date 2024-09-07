package com.numberone.daepiro.domain.file.entity

import com.numberone.daepiro.domain.baseentity.PrimaryKeyEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Entity
@Table(name = "`file`")
class FileEntity(
    path: String,
    documentType: FileDocumentType,
    documentId: Long
) : PrimaryKeyEntity() {
    @Column(nullable = false)
    var path = path
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255)")
    var documentType = documentType
        protected set

    @Column(nullable = false)
    var documentId = documentId
        protected set
}

enum class FileDocumentType {
    ARTICLE, DONATION, USER_PROFILE
}
