package com.numberone.daepiro.domain.behaviourTip.dto.response

data class GetBehaviourTipResponse(
    val disasterType: String,
    val tips: List<FilterResponse>
) {
    companion object {
        fun of(
            disasterType: String,
            tips: List<FilterResponse>
        ): GetBehaviourTipResponse {
            return GetBehaviourTipResponse(
                disasterType = disasterType,
                tips = tips
            )
        }
    }
}

data class FilterResponse(
    val filter: String,
    val tips: List<String>
) {
    companion object {
        fun of(
            filter: String,
            tips: List<String>
        ): FilterResponse {
            return FilterResponse(
                filter = filter,
                tips = tips
            )
        }
    }
}
