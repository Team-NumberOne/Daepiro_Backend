package com.numberone.daepiro.domain.file.service

import com.numberone.daepiro.domain.file.repository.FileRepository
import com.numberone.daepiro.infra.s3.S3Uploader
import org.springframework.stereotype.Service

@Deprecated("Deprecated. File 관련 엔드포인트를 분리하지 않는다.")
@Service
class FileDispatcher(
    private val fileRepository: FileRepository,
    private val s3Uploader: S3Uploader,
)
