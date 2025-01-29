package com.numberone.daepiro.domain.mypage.dto.request

import com.numberone.daepiro.domain.user.dto.request.AddressRequest
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Size

data class EditAddressesRequest(
    @field:Size(min = 1, max = 3, message = "주소는 1개 이상 3개 이하로 입력해주세요.")
    val addresses: List<AddressRequest>
)
