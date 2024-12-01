package com.numberone.daepiro.infra.s3

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Component
class S3Uploader(
    @Value("\${s3.bucket}")
    private val bucket: String,
    private val s3Client: AmazonS3Client,
) {
    fun uploadFile(file: MultipartFile, path: String): String {
        val fileName = "$path/${UUID.randomUUID()}_${file.originalFilename}"

        val metadata = ObjectMetadata().apply {
            contentLength = file.size
            contentType = file.contentType
        }

        s3Client.putObject(
            PutObjectRequest(bucket, fileName, file.inputStream, metadata)
        )

        return s3Client.getUrl(bucket, fileName).toString()
    }

    fun uploadFiles(files: List<MultipartFile>, path: String): List<String> {
        return buildList {
            files.forEach { file ->
                add(uploadFile(file, path))
            }
        }
    }

    companion object {
        const val COMMUNITY_FILES = "community/files/"
    }
}
