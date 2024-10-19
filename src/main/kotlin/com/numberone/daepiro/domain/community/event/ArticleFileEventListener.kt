package com.numberone.daepiro.domain.community.event

import com.numberone.daepiro.infra.s3.S3Uploader
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import org.springframework.web.multipart.MultipartFile

@Component
class ArticleFileEventListener(
    private val s3Uploader: S3Uploader,
) {
    @Async("asyncExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onArticleFileUpload(event: ArticleFileUploadEvent) {
        s3Uploader.uploadFiles(event.files, "article")
    }
}

data class ArticleFileUploadEvent(
    val articleId: Long,
    val files: List<MultipartFile>,
)
