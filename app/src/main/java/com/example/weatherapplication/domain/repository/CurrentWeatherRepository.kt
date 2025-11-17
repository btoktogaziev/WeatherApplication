package com.example.weatherapplication.domain.repository

import com.example.weatherapplication.domain.model.CurrentWeather

interface CurrentWeatherRepository {
    suspend fun getCurrentWeather(
        cityName: String
    ): CurrentWeather
}