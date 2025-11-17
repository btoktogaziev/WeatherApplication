package com.example.weatherapplication.data.network.model.weather

import com.example.weatherapplication.domain.model.ForecastWeather
import com.google.gson.annotations.SerializedName

data class ForecastWeatherListDto(
    @SerializedName("list")
    val forecastList: List<ForecastWeatherItemDto>
)

data class ForecastWeatherItemDto(
    val weather: List<Weather>,
    @SerializedName("dt_txt")
    val dateTime: String,
    @SerializedName("main")
    val main: Main,
)

data class City(
    val name: String
)

fun ForecastWeatherItemDto.toDomain(): ForecastWeather {
    return ForecastWeather(
        minTemp = main.tempMin,
        maxTemp = main.tempMax,
        icon = weather.first().icon.replace("n","d"),
        weather = weather.first().main,
        date = dateTime
    )
}