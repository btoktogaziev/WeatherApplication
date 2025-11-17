package com.example.weatherapplication.data.network.model.weather

import com.example.weatherapplication.domain.model.CurrentWeather
import com.google.gson.annotations.SerializedName

data class CurrentWeatherDto(
    val name: String,
    val weather: List<Weather>,
    @SerializedName("dt")
    val dateTime: Long,
    val main: Main,
    val wind: Wind
)

data class Main(
    val temp: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    val humidity: Int
)

data class Wind(
    @SerializedName("speed")
    val windSpeed: Double
)

data class Weather(
    val main: String,
    val description: String,
    val icon: String
)

fun CurrentWeatherDto.toDomain(): CurrentWeather {
    val weatherInfo = weather.first()
    return CurrentWeather(
        cityName = name,
        temp = main.temp,
        feelsLike = main.feelsLike,
        humidity = main.humidity,
        windSpeed = wind.windSpeed,
        icon = weatherInfo.icon,
        weather = weatherInfo.main,
        description = weatherInfo.description
    )
}
