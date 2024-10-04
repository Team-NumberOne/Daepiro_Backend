package com.numberone.daepiro.domain.dataCollecter.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class SaveDisastersRequest(
    val disasters: List<DisasterRequest>
)

data class DisasterRequest(
    @Schema(description = "재난 발생 시간", example = "2021-08-01T00:00:00")
    val generatedAt: LocalDateTime,

    @Schema(description = "재난 메시지 ID", example = "1")
    val messageId: Long,

    @Schema(description = "재난 메시지", example = "연안사고 주의보 발령. 동해안 높은 파도가 예상되므로 방파제·갯바위·해안가 등 접근 및 입수 금지, 연안 안전사고에 주의 바랍니다. [포항시]")
    val message: String,

    @Schema(description = "위치 ID", example = "2")
    val locationId: Long,
)
