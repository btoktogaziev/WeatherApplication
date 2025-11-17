package com.example.weatherapplication.presentation.ui.screens

sealed class Screens(val route: String) {
    data object CurrentWeatherScreen: Screens("CurrentWeatherScreen")
    data object ForecastWeatherScreen: Screens("ForecastWeatherScreen"){
        fun createRoute(cityName: String) = "ForecastWeatherScreen/$cityName"
    }
}