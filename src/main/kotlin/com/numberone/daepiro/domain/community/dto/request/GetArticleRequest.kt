package com.numberone.daepiro.domain.community.dto.request

import com.numberone.daepiro.domain.community.entity.ArticleCategory
import com.numberone.daepiro.domain.community.entity.ArticleStatus
import com.numberone.daepiro.domain.community.entity.ArticleType
import com.numberone.daepiro.domain.user.entity.UserEntity
import io.swagger.v3.oas.annotations.media.Schema
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Pageable

@ParameterObject
@Schema(description = "아티클 리스트 조회 요청입니다.")
data class GetArticleRequest(
    @Schema(description = "게시글 구분. <br>- DONGNE: 동네생활 <br>- INFORMATION: 정보 <br>- DISASTER: 재난상황", example = "DONGNE")
    val type: ArticleType? = null,
    @Schema(description = "게시글 카테고리. <br>- LIFE: 일상 <br>- TRAFFIC: 교통 <br>- SAFE: 안전 <br>- OTHER: 기타", example = "LIFE")
    val category: ArticleCategory? = null,
    @Schema(description = "게시글 상태. <br>- DELETE: 삭제 <br>- ACTIVE: 삭제되지 않음", example = "ACTIVE")
    val status: ArticleStatus? = null,
    @Schema(description = "주소", example = "서울특별시 강동구 천호동")
    val address: String? = null,
    @Schema(description = "페이지 번호", example = "1")
    val page: Int,
    @Schema(description = "페이지 사이즈", example = "10")
    val size: Int,
) {
    init {
        require(page > 0 && size > 0) {
            "page 와 size 는 0 보다 큰 값으로 요청해주세요"
        }
    }

    fun getPageable(): Pageable {
        return Pageable.ofSize(size).withPage(page - 1)
    }

    companion object {
        fun ofHomeFeed(category: ArticleCategory?, user: UserEntity) = GetArticleRequest(
            type = ArticleType.DONGNE,
            category = category,
            status = ArticleStatus.ACTIVE,
            address = user.address?.toFullAddress(),
            page = 1,
            size = 100//최근 100개를 일단 가져오고 서비스로직에서 인기순으로 15개만 자름
        )
    }
}

