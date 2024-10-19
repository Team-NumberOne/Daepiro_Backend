package com.numberone.daepiro.domain.file.api

import com.numberone.daepiro.domain.file.dto.response.FileSimpleResponse
import com.numberone.daepiro.global.dto.ApiResult
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@Tag(name = "File API", description = "파일 관련 API")
@RequestMapping("/v1/files")
interface FileApiV1 {
    @PostMapping("/articles/{articleId}")
    @Operation(summary = "파일 업로드", description = "")
    fun uploadArticleFile(
        @PathVariable articleId: String,
        @RequestParam image: MultipartFile,
    ): ApiResult<FileSimpleResponse>
}
