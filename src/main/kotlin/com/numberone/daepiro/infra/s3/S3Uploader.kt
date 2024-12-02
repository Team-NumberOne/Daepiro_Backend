package com.numberone.daepiro.infra.s3

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.numberone.daepiro.domain.file.model.RawFile
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.util.*

@Component
class S3Uploader(
    @Value("\${s3.bucket}")
    private val bucket: String,
    private val s3Client: AmazonS3Client,
) {
    fun uploadFile(file: RawFile, path: String): String {
        val fileName = "$path/${UUID.randomUUID()}_${file.fileName}"

        val metadata = ObjectMetadata().apply {
            contentLength = file.content.size.toLong()
        }

        ByteArrayInputStream(file.content).use { inputStream ->
            s3Client.putObject(
                PutObjectRequest(bucket, fileName, inputStream, metadata)
            )
        }

        return s3Client.getUrl(bucket, fileName).toString()
    }

    fun uploadFiles(files: List<RawFile>, path: String): List<String> {
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
