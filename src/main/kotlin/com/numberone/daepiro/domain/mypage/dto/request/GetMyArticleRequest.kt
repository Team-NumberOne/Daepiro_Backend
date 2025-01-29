package com.numberone.daepiro.domain.mypage.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Pageable

@ParameterObject
data class GetMyArticleRequest(
    @Schema(description = "페이지 번호(1부터 시작)", example = "1")
    val page: Int,
    @Schema(description = "페이지 사이즈", example = "10")
    val size: Int,
){
    fun getPageable(): Pageable {
        return Pageable.ofSize(size).withPage(page - 1)
    }
}
