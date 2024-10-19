package com.numberone.daepiro.domain.file.repository

import com.numberone.daepiro.domain.file.entity.FileEntity
import org.springframework.data.jpa.repository.JpaRepository

@Deprecated("Deprecated. File 관련 엔드포인트를 분리하지 않는다.")
interface FileRepository : JpaRepository<FileEntity, Long>
