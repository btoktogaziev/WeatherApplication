package com.example.weatherapplication.data.repository

import com.example.weatherapplication.data.network.api.WeatherApiService
import com.example.weatherapplication.data.network.model.weather.toDomain
import com.example.weatherapplication.domain.model.CurrentWeather
import com.example.weatherapplication.domain.model.ForecastWeather
import com.example.weatherapplication.domain.repository.CurrentWeatherRepository
import com.example.weatherapplication.domain.repository.ForecastWeatherRepository
import okio.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApiService: WeatherApiService
) : CurrentWeatherRepository, ForecastWeatherRepository {

    override suspend fun getCurrentWeather(
        cityName: String
    ): CurrentWeather {
        try {
            val response = weatherApiService.getCurrentWeather(cityName)
            if (response.isSuccessful) {
                val body = response.body()
                return body?.toDomain() ?: throw Exception("Empty response from server")
            } else {
                when (response.code()) {
                    404 -> throw Exception("Город не найден")
                    401 -> throw Exception("Неверный ключ API")
                    else -> throw Exception("Ошибка сервера: ${response.code()}")
                }
            }
        } catch (e: IOException) {
            throw IOException("Нет подключения к интернету")
        } catch (e: Exception) {
            throw Exception("Неизвестная ошибка: ${e.localizedMessage}")

        }
    }

    override suspend fun getForecastWeather(
        cityName: String
    ): List<ForecastWeather> {
        val dto = weatherApiService.getForecastWeather(cityName)
        return dto.forecastList.map { it.toDomain() }
    }


}