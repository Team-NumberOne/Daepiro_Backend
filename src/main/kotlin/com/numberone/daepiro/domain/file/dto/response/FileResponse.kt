package com.numberone.daepiro.domain.file.dto.response

import com.numberone.daepiro.domain.file.entity.FileEntity
import java.time.LocalDateTime

data class FileSimpleResponse(
    val id: Long,
    val path: String,
    val createdAt: LocalDateTime,
    val deletedAt: LocalDateTime?,
) {
    companion object {
        fun from(file: FileEntity): FileSimpleResponse {
            val fileId = file.id
            require(fileId != null)
            return FileSimpleResponse(
                id = fileId,
                path = file.path,
                createdAt = file.createdAt,
                deletedAt = file.deletedAt,
            )
        }
    }
}
