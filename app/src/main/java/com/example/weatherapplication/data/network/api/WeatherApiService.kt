package com.example.weatherapplication.data.network.api

import com.example.weatherapplication.data.network.model.weather.CurrentWeatherDto
import com.example.weatherapplication.data.network.model.weather.ForecastWeatherListDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val APP_ID = "3f9b8d82f7b3b7ecd9f29250e384b1b6"

interface WeatherApiService {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") cityName: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en_ru",
        @Query("appid") appid: String = APP_ID
    ): Response<CurrentWeatherDto>

    @GET("forecast")
    suspend fun getForecastWeather(
        @Query("q") cityName: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en_ru",
        @Query("appid") appid: String = APP_ID
    ): ForecastWeatherListDto
}
