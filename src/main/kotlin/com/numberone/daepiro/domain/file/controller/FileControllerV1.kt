package com.numberone.daepiro.domain.file.controller

import com.numberone.daepiro.domain.file.api.FileApiV1
import com.numberone.daepiro.domain.file.dto.response.FileSimpleResponse
import com.numberone.daepiro.global.dto.ApiResult
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class FileControllerV1() : FileApiV1 {
    override fun uploadArticleFile(articleId: String, image: MultipartFile): ApiResult<FileSimpleResponse> {
        TODO("Not yet implemented")
    }
}
