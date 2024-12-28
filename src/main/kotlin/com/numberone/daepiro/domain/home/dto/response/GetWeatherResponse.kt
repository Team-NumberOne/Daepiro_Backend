package com.numberone.daepiro.domain.home.dto.response

data class GetWeatherResponse(
    val currentPosition: String,
    val todayWeather:String
){
    companion object {
        fun of(
            currentPosition: String,
            todayWeather: String
        ): GetWeatherResponse {
            return GetWeatherResponse(
                currentPosition = currentPosition,
                todayWeather = todayWeather
            )
        }
    }
}
