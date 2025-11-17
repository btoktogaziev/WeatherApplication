package com.example.weatherapplication.domain.model

data class ForecastWeather(
    val minTemp: Double,
    val maxTemp: Double,
    val icon: String,
    val weather: String,
    val date: String
)