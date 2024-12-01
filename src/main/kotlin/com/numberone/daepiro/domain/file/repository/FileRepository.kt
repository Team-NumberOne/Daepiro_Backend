package com.numberone.daepiro.domain.file.repository

import com.numberone.daepiro.domain.file.entity.FileEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FileRepository : JpaRepository<FileEntity, Long>
