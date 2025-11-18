package com.example.weatherapplication.data.network.api

import com.example.weatherapplication.BuildConfig.API_KEY
import com.example.weatherapplication.data.network.model.weather.CurrentWeatherDto
import com.example.weatherapplication.data.network.model.weather.ForecastWeatherListDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") cityName: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en_ru",
        @Query("appid") appid: String = API_KEY
    ): Response<CurrentWeatherDto>

    @GET("forecast")
    suspend fun getForecastWeather(
        @Query("q") cityName: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en_ru",
        @Query("appid") appid: String = API_KEY
    ): ForecastWeatherListDto
}
