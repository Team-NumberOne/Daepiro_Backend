package com.numberone.daepiro.domain.community.event

import com.numberone.daepiro.domain.file.entity.FileDocumentType
import com.numberone.daepiro.domain.file.entity.FileEntity
import com.numberone.daepiro.domain.file.model.RawFile
import com.numberone.daepiro.domain.file.repository.FileRepository
import com.numberone.daepiro.global.utils.TransactionUtils
import com.numberone.daepiro.infra.s3.S3Uploader
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import org.springframework.web.multipart.MultipartFile

@Component
class ArticleFileEventListener(
    private val s3Uploader: S3Uploader,
    private val fileRepository: FileRepository,
) {
    @Async("asyncExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onArticleFileUpload(event: ArticleFileUploadEvent) {
        val paths = s3Uploader.uploadFiles(event.files, "article")

        TransactionUtils.writable {
            val files = paths.map {
                FileEntity.of(
                    path = it,
                    documentType = FileDocumentType.ARTICLE,
                    documentId = event.articleId,
                )
            }
            fileRepository.saveAll(files)
        }
    }
}

data class ArticleFileUploadEvent(
    val articleId: Long,
    val files: List<RawFile>,
)
