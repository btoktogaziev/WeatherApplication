package com.example.weatherapplication.domain.usecase

import com.example.weatherapplication.domain.model.ForecastWeather
import com.example.weatherapplication.domain.repository.ForecastWeatherRepository
import javax.inject.Inject

class GetForecastWeatherUseCase @Inject constructor(
    private val forecastWeatherRepository: ForecastWeatherRepository
) {
    suspend operator fun invoke(cityName: String): List<ForecastWeather> {
        return forecastWeatherRepository.getForecastWeather(cityName)
    }
}