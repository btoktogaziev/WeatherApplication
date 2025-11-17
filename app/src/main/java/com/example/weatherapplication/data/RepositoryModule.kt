package com.example.weatherapplication.data

import com.example.weatherapplication.data.repository.WeatherRepositoryImpl
import com.example.weatherapplication.domain.repository.CurrentWeatherRepository
import com.example.weatherapplication.domain.repository.ForecastWeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindCurrentWeatherRepository(impl: WeatherRepositoryImpl): CurrentWeatherRepository

    @Binds
    @Singleton
    fun bindForecastWeatherRepository(impl: WeatherRepositoryImpl): ForecastWeatherRepository
}