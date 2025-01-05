package com.numberone.daepiro.domain.sponsor.dto.request

import jakarta.validation.constraints.NotBlank

data class CheeringRequest(
    @NotBlank
    val content: String
)
