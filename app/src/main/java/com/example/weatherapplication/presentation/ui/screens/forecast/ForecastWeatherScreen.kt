package com.example.weatherapplication.presentation.ui.screens.forecast

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.weatherapplication.R
import com.example.weatherapplication.presentation.ui.theme.Typography
import kotlin.math.roundToInt

@Composable
fun ForecastWeatherScreen(
    cityName: String,
    viewModel: ForecastWeatherViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.init(cityName)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.isLoading) {
            Text("Loading...")
            CircularProgressIndicator()
        } else {
            LazyRow {
                items(uiState.weatherList) {
                    ItemForecast(
                        main = it.weather,
                        tempMin = it.minTemp.roundToInt().toString(),
                        tempMax = it.maxTemp.roundToInt().toString(),
                        date = it.date,
                        icon = it.icon
                    )
                    Spacer(Modifier.size(8.dp))
                }
            }
        }
    }
}

@Composable
fun ItemForecast(main: String, tempMin: String, tempMax: String, date: String, icon: String) {
    Column(
        modifier = Modifier
            .width(260.dp)
            .height(240.dp)
            .background(shape = RoundedCornerShape(12.dp), color = Color.White)
            .border(shape = RoundedCornerShape(12.dp), width = 2.dp, color = Color.Gray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                    color = Color.Red
                )
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = date,
                style = Typography.displaySmall,
                color = Color.White
            )
        }
        Row(
            modifier = Modifier.padding(12.dp)
        ) {
            AsyncImage("https://openweathermap.org/img/wn/${icon}@2x.png", "icon weather")
            Text(main, style = Typography.titleLarge)
        }
        Text(
            "Min: $tempMin${stringResource(R.string.Celsius)}",
            style = Typography.headlineSmall
        )
        Spacer(Modifier.height(20.dp))
        Text(
            "Max: $tempMax${stringResource(R.string.Celsius)}",
            style = Typography.headlineSmall
        )
    }
}