package com.example.weatherapplication.domain.repository

import com.example.weatherapplication.domain.model.ForecastWeather

interface ForecastWeatherRepository {
    suspend fun getForecastWeather(cityName: String): List<ForecastWeather>
}