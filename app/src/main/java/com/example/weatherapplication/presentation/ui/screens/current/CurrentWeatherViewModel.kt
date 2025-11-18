package com.example.weatherapplication.presentation.ui.screens.current

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.data.datastore.DataStoreManager
import com.example.weatherapplication.domain.usecase.GetCurrentWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    data class DefaultUiState(
        val cityName: String = "",
        val icon: String = "-",
        val temp: String = "-",
        val weatherState: String = "",
        val feelsLike: String = "-",
        val humidity: String = "-",
        val weatherDescription: String = "-",
        val windSpeed: String = "-",
        val isLoading: Boolean = false,
        val error: String? = null,
        val isRefreshing: Boolean = false
    )

    private val _uiState = MutableStateFlow(DefaultUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<CurrentWeatherEvent>()
    val event = _event.receiveAsFlow()

    private var isInitialized = false

    fun init() {
        if (isInitialized) return
        isInitialized = true

        viewModelScope.launch {
            dataStoreManager.city.collect { savedCity ->
                if (savedCity.isNotBlank()) {
                    _uiState.update { it.copy(cityName = savedCity) }
                    loadWeather(savedCity)
                }
            }
        }
    }

    fun onFieldChanged(city: String) {
        _uiState.update { it.copy(cityName = city) }
    }

    fun loadWeather(city: String, isRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = !isRefresh,
                    isRefreshing = isRefresh,
                    error = null
                )
            }
            try {
                val weather = getCurrentWeatherUseCase.invoke(city)
                _uiState.update {
                    it.copy(
                        cityName = weather.cityName,
                        icon = "https://openweathermap.org/img/wn/${weather.icon}@2x.png",
                        temp = weather.temp.roundToInt().toString(),
                        weatherState = weather.weather,
                        feelsLike = weather.feelsLike.roundToInt().toString(),
                        humidity = weather.humidity.toString(),
                        weatherDescription = weather.description,
                        windSpeed = weather.windSpeed.toString(),
                        isLoading = false,
                        isRefreshing = false,
                        error = null
                    )
                }
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        error = e.message ?: "Нет подключения к интернету"
                    )
                }
                _event.send(CurrentWeatherEvent.ShowError("Нет подключения к интернету"))
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        error = e.message ?: "Неизвестная ошибка"
                    )
                }
                _event.send(CurrentWeatherEvent.ShowError(e.message ?: "Неизвестная ошибка"))
            }
        }
    }

    fun onSearchClicked() {
        val city = _uiState.value.cityName
        if (city.isNotBlank()) {
            viewModelScope.launch {
                loadWeather(city)
                dataStoreManager.saveCity(city)
            }
        }
    }

    fun onUpdate() {
        val city = _uiState.value.cityName
        if (city.isNotBlank()) {
            loadWeather(city, isRefresh = true)
        }
    }
}

sealed interface CurrentWeatherEvent {
    data class NavigateToForecast(val cityName: String) : CurrentWeatherEvent
    data class ShowError(val message: String) : CurrentWeatherEvent
}