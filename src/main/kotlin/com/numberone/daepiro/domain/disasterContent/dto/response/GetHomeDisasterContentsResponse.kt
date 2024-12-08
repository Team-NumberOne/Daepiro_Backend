package com.numberone.daepiro.domain.disasterContent.dto.response

import com.numberone.daepiro.domain.dataCollecter.entity.News

data class GetHomeDisasterContentsResponse(
    val contents: List<DisasterContentResponse>
){
    companion object {
        fun of(
            news: List<News>
        ): GetHomeDisasterContentsResponse {
            return GetHomeDisasterContentsResponse(
                contents = news.map { DisasterContentResponse.of(it) }
            )
        }
    }
}
