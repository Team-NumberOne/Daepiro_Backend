package com.numberone.daepiro.domain.community.dto.response

import com.numberone.daepiro.domain.community.entity.Article
import com.numberone.daepiro.domain.community.entity.ArticleCategory
import com.numberone.daepiro.domain.community.entity.ArticleStatus
import com.numberone.daepiro.domain.file.entity.FileEntity
import com.numberone.daepiro.domain.file.entity.toPaths
import com.numberone.daepiro.domain.user.entity.UserEntity
import com.querydsl.core.annotations.QueryProjection
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "리스트 아티클 응답 모델")
data class ArticleListResponse @QueryProjection constructor(
    @Schema(description = "아티클 ID", example = "1")
    val id: Long,
    @Schema(description = "아티클 상태 <br> - DELETED: 삭제 <br> - ACTIVE: 활성화")
    val status: ArticleStatus,
    @Schema(description = "아티클 타입 | 동네생활, 정보, 재난상황")
    val type: String,
    @Schema(description = "아티클 카테고리 | LIFE(일상), TRAFFIC(교통), SAFE(안전, 치안), OTHER(기타)")
    val category: ArticleCategory,
    @Schema(description = "아티클 제목", example = "title")
    val title: String,
    @Schema(description = "아티클 내용", example = "body")
    val body: String,
    @Schema(description = "좋아요 수", example = "100")
    val likeCount: Int = 0,
    @Schema(description = "조회수", example = "200")
    val viewCount: Int = 0,
    @Schema(description = "댓글 수", example = "50")
    val commentCount: Int = 0,
    @Schema(description = "신고 횟수", example = "2")
    val reportCount: Int = 0,
    @Schema(description = "주소 정보")
    val address: AddressResponse? = null,
    @Schema(description = "생성일시", example = "2024-12-02T21:48:14.929554")
    val createdAt: LocalDateTime? = null,
    @Schema(description = "수정일시", example = "2024-12-02T21:48:14.929554")
    val lastModifiedAt: LocalDateTime? = null,
    @Schema(description = "작성자 정보")
    val authorUser: AuthorResponse? = null,
) {
    @Schema(description = "미리보기 이미지", example = "[\"https://path/to/file1\", \"https://path/to/file2\"]")
    var previewImageUrl: String? = null

    fun updatePreviewImageUrl(previewImageUrl: String) {
        this.previewImageUrl = previewImageUrl
    }
}

data class AddressResponse @QueryProjection constructor(
    val addressId: Long? = null,
    val siDo: String? = null,
    val siGunGu: String? = null,
)

@Schema(description = "간단한 아티클 응답 모델")
data class ArticleSimpleResponse(
    @Schema(description = "아티클 ID", example = "1")
    val id: Long,
) {
    companion object {
        fun from(
            article: Article
        ): ArticleSimpleResponse {
            return ArticleSimpleResponse(
                id = article.id!!
            )
        }
    }
}

@Schema(description = "상세 아티클 응답 모델")
data class ArticleDetailResponse(
    @Schema(description = "아티클 ID", example = "1")
    val id: Long,

    @Schema(description = "아티클 제목", example = "title")
    val title: String,

    @Schema(description = "아티클 내용", example = "body")
    val body: String,

    @Schema(description = "아티클 타입 | 동네생활, 정보, 재난상황")
    val type: String,

    @Schema(description = "아티클 카테고리 | LIFE(일상), TRAFFIC(교통), SAFE(안전, 치안), OTHER(기타)")
    val category: ArticleCategory,

    @Schema(description = "위치 정보 공개 여부", example = "true")
    val isLocationVisible: Boolean,

    @Schema(description = "좋아요 수", example = "100")
    val likeCount: Int = 0,

    @Schema(description = "조회수", example = "200")
    val viewCount: Int = 0,

    @Schema(description = "댓글 수", example = "50")
    val commentCount: Int = 0,

    @Schema(description = "신고 횟수", example = "2")
    val reportCount: Int = 0,

    @Schema(description = "아티클 상태")
    val status: ArticleStatus,

    @Schema(description = "작성자 정보")
    val authorUser: AuthorResponse? = null,

    @Schema(description = "파일 목록", example = "[\"https://path/to/file1\", \"https://path/to/file2\"]")
    val files: List<String>?,

    @Schema(description = "해당 게시글에 작성된 댓글 목록 (계층형)")
    val comments: List<CommentResponse> = emptyList(),

    @Schema(description = "생성일시", example = "2024-12-02T21:48:14.929554")
    val createdAt: LocalDateTime? = null,

    @Schema(description = "수정일시", example = "2024-12-02T21:48:14.929554")
    val lastModifiedAt: LocalDateTime? = null,
) {
    companion object {
        fun of(
            article: Article,
            files: List<FileEntity>? = emptyList(),
            comments: List<CommentResponse> = emptyList()
        ): ArticleDetailResponse {
            return ArticleDetailResponse(
                id = article.id!!,
                title = article.title,
                body = article.body,
                type = article.type.description,
                category = article.category,
                isLocationVisible = article.isLocationVisible,
                likeCount = article.likeCount,
                viewCount = article.viewCount,
                commentCount = article.commentCount,
                reportCount = article.reportCount,
                status = article.status,
                authorUser = article.authUser?.let { AuthorResponse.from(it) },
                files = files?.toPaths(),
                comments = comments,
                createdAt = article.createdAt,
                lastModifiedAt = article.lastModifiedAt,
            )
        }
    }
}

@Schema(description = "작성자 응답 모델")
data class AuthorResponse @QueryProjection constructor(
    @Schema(description = "사용자 ID", example = "10")
    val userId: Long,

    @Schema(description = "닉네임", example = "abcd")
    val nickname: String? = null,

    @Schema(description = "실명", example = "abcd")
    val realname: String? = null,

    @Schema(description = "온보딩 완료 여부", example = "true")
    val isCompletedOnboarding: Boolean
) {
    companion object {
        fun from(user: UserEntity): AuthorResponse {
            return AuthorResponse(
                userId = user.id!!,
                nickname = user.nickname,
                realname = user.realname,
                isCompletedOnboarding = user.isCompletedOnboarding
            )
        }
    }
}

@Schema(description = "댓글 응답 모델")
data class CommentResponse @QueryProjection constructor(
    @Schema(description = "댓글 아이디(PK)")
    val id: Long,
    @Schema(description = "댓글 본문")
    val body: String,
    @Schema(description = "작성자 정보")
    val author: AuthorResponse? = null,
    @Schema(description = "좋아요 개수")
    val likeCount: Int = 0,
    @Schema(description = "부모 댓글 Id")
    val parentCommentId: Long? = null,
    @Schema(description = "생성일시", example = "2024-12-02T21:48:14.929554")
    val createdAt: LocalDateTime? = null,
    @Schema(description = "수정일시", example = "2024-12-02T21:48:14.929554")
    val lastModifiedAt: LocalDateTime? = null,
) {
    @Schema(description = "해당 댓글에 자식으로 포함된 댓글들")
    var children: MutableList<CommentResponse> = mutableListOf()
}
