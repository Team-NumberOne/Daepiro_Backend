package com.numberone.daepiro.domain.user.dto.response

import com.numberone.daepiro.domain.disaster.entity.Disaster
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class DisasterResponse(
    @Schema(description = "재난 종류", example = "호우")
    val disasterType: String,

    @Schema(description = "제목", example = "서울시 성북구 쌍문동 호우 발생")
    val title: String,

    @Schema(description = "내용", example = "금일 10.23. 19:39경 소촌동 855 화재 발생, 인근주민은 안전유의 및 차량우회바랍니다. 960-8222")
    val content: String,

    @Schema(description = "발생 시간", example = "2024-10-23T19:53:00")
    val time: LocalDateTime
) {
    companion object {
        fun of(disaster: Disaster): DisasterResponse {
            val address =
                return DisasterResponse(
                    disasterType = disaster.disasterType.type.korean,
                    title = "${disaster.location.toAddress()} ${disaster.disasterType.type.korean} 발생",
                    content = disaster.message,
                    time = disaster.generatedAt
                )
        }
    }
}
