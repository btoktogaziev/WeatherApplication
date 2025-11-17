package com.example.weatherapplication.presentation.ui.screens.forecast

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.domain.model.ForecastWeather
import com.example.weatherapplication.domain.usecase.GetForecastWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ForecastWeatherViewModel @Inject constructor(
    private val getForecastWeatherUseCase: GetForecastWeatherUseCase,
) : ViewModel() {
    data class DefaultUiState(
        val cityName: String = "",
        val weatherList: List<ForecastWeather> = emptyList(),
        val isLoading: Boolean = false
    )

    private val _uiState = MutableStateFlow(DefaultUiState())
    val uiState: StateFlow<DefaultUiState> = _uiState.asStateFlow()

    private var isInitialized = false

    fun init(cityName: String) {
        if (isInitialized) return
        isInitialized = true
        _uiState.update { it.copy(cityName = cityName) }
        observeData(cityName)
    }

    fun observeData(cityName: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val weather = getForecastWeatherUseCase(cityName)
                val dailyWeather = weather
                    .groupBy { it.date.substringBefore(" ") }
                    .map { (day, items) ->
                        ForecastWeather(
                            minTemp = items.minOf { it.minTemp },
                            maxTemp = items.maxOf { it.maxTemp },
                            icon = items.first().icon,
                            weather = items.first().weather,
                            date = formatDate(day)
                        )
                    }
                _uiState.update {
                    it.copy(
                        cityName = cityName,
                        weatherList = dailyWeather,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                Log.e("oshibka", "Error loading forecast", e)
                _uiState.update { it.copy(cityName = "Error ${e.message}", isLoading = false) }
            }
        }
    }
}

@SuppressLint("NewApi")
fun formatDate(raw: String): String {
    val input = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US)
    val output = DateTimeFormatter.ofPattern("EEE, dd MMM", Locale.US)
    val date = LocalDate.parse(raw, input)
    return date.format(output)
}