package com.example.weatherapplication.domain.usecase

import com.example.weatherapplication.domain.model.CurrentWeather
import com.example.weatherapplication.domain.repository.CurrentWeatherRepository
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val currentWeatherRepository: CurrentWeatherRepository
) {
    suspend operator fun invoke(cityName: String): CurrentWeather {
        return currentWeatherRepository.getCurrentWeather(cityName)
    }
}