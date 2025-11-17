package com.example.weatherapplication.domain.model

data class CurrentWeather(
    val cityName: String,
    val temp: Double,
    val feelsLike: Double,
    val humidity: Int,
    val windSpeed: Double,
    val icon: String,
    val weather: String,
    val description: String
)