package com.numberone.daepiro.domain.file.service

import com.numberone.daepiro.domain.file.repository.FileRepository
import com.numberone.daepiro.infra.s3.S3Uploader
import org.springframework.stereotype.Service

@Service
class FileDispatcher(
    private val fileRepository: FileRepository,
    private val s3Uploader: S3Uploader,
)
