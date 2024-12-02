package com.numberone.daepiro.domain.file.model

import org.joda.time.LocalDateTime
import org.springframework.web.multipart.MultipartFile

data class RawFile(
    val fileName: String,
    val content: ByteArray
) {
    companion object {
        fun of(multipartFile: MultipartFile): RawFile {
            return RawFile(
                fileName = multipartFile.originalFilename ?: "file/${LocalDateTime.now()}",
                content = multipartFile.bytes,
            )
        }
    }
}
