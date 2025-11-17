package com.example.weatherapplication.presentation.ui.screens.current

import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.weatherapplication.R
import com.example.weatherapplication.presentation.ui.theme.ProgressBarBlue
import com.example.weatherapplication.presentation.ui.theme.ProgressBarCold
import com.example.weatherapplication.presentation.ui.theme.ProgressBarGreen
import com.example.weatherapplication.presentation.ui.theme.ProgressBarHot
import com.example.weatherapplication.presentation.ui.theme.TransparentWhite
import com.example.weatherapplication.presentation.ui.theme.Typography
import kotlin.math.abs

@Composable
fun CurrentWeatherScreen(
    toForecastScreen: (String) -> Unit,
    viewModel: CurrentWeatherViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.init()
    }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.event.collect { e ->
            when (e) {
                is CurrentWeatherEvent.NavigateToForecast ->
                    toForecastScreen(e.cityName)

                is CurrentWeatherEvent.ShowError ->
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    val state = rememberPullToRefreshState()
    val scrollState = rememberScrollState()
    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = viewModel::onUpdate,
        state = state,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                state = state,
                isRefreshing = uiState.isRefreshing,
                containerColor = ProgressBarBlue,
                color = Color.White
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (uiState.isLoading) {
                Text("Loading...")
                CircularProgressIndicator()
            } else {
                MainInfoSection(
                    temp = uiState.temp,
                    weatherMain = uiState.weatherState,
                    weatherIcon = uiState.icon,
                    description = uiState.weatherDescription,
                )
                Spacer(Modifier.size(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedTextField(
                        modifier = Modifier,
                        singleLine = true,
                        value = uiState.cityName,
                        onValueChange = viewModel::onFieldChanged,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Gray,
                            unfocusedContainerColor = Color.White,
                            unfocusedPlaceholderColor = Color.Gray,
                            unfocusedTextColor = Color.Black,
                            focusedContainerColor = Color.White,
                            focusedBorderColor = Color.Black,
                            focusedTextColor = Color.Black
                        ),
                        placeholder = {
                            Text("Enter the city")
                        },
                    )
                    IconButton(
                        modifier = Modifier
                            .background(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White
                            )
                            .border(
                                width = 2.dp,
                                shape = RoundedCornerShape(12.dp),
                                color = Color.LightGray
                            ),
                        onClick = viewModel::onSearchClicked
                    ) {
                        Icon(Icons.Default.Search, "icon search")
                    }
                }
                Spacer(Modifier.size(20.dp))
                ForecastSection { toForecastScreen(uiState.cityName) }
                AdditionalInfoSection(
                    feelsLike = uiState.feelsLike,
                    humidity = uiState.humidity,
                    windSpeed = uiState.windSpeed
                )
            }
        }
    }
}

@Composable
fun MainInfoSection(
    temp: String,
    weatherMain: String,
    weatherIcon: String,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(shape = RoundedCornerShape(12.dp), color = TransparentWhite)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = "$temp${stringResource(R.string.Celsius)}",
                style = Typography.displayLarge
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = weatherIcon,
                    contentDescription = "icon weather"
                )
                Text(
                    text = weatherMain,
                    style = Typography.displaySmall
                )
                Text(
                    text = description,
                    style = Typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun ForecastSection(
    toForecastWeatherScreen: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(shape = RoundedCornerShape(12.dp), color = Color.White)
            .border(width = 2.dp, color = Color.LightGray, shape = RoundedCornerShape(12.dp))
            .clickable(
                enabled = true,
                onClick = {
                    toForecastWeatherScreen()
                })
            .padding(all = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Cloud, contentDescription = "icon cloud", tint = Color.Gray)
        Text(
            text = "5-day weather forecast", style = Typography.headlineSmall
        )
        Icon(
            Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "icon arrow right", tint = Color.Gray
        )
    }
}

@Composable
fun AdditionalInfoSection(
    feelsLike: String,
    humidity: String,
    windSpeed: String
) {
    Column(
        modifier = Modifier
            .padding(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            FeelsLikeTempSection(feelsLike)
            Spacer(Modifier.size(8.dp))
            HumiditySection(humidity)
        }
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            WindSpeedSection(windSpeed)
        }
    }
}

@Composable
fun FeelsLikeTempSection(
    feelsLike: String,
) {
    val feels = feelsLike.toFloatOrNull() ?: 0f
    val progress = (abs(feels) / 40f).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(4000, easing = FastOutSlowInEasing),
        label = "temp animation",
    )
    Column(
        modifier = Modifier
            .size(180.dp)
            .background(shape = RoundedCornerShape(12.dp), color = Color.White)
            .border(width = 2.dp, color = Color.LightGray, shape = RoundedCornerShape(12.dp))
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Feels like",
            style = Typography.headlineMedium
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = "$feelsLike${stringResource(R.string.Celsius)}",
            style = Typography.displayMedium,
            color = if (feels <= 0) ProgressBarCold else ProgressBarHot,
        )
        Spacer(Modifier.height(20.dp))
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .width(100.dp)
                .height(12.dp),
            color = if (feels <= 0) ProgressBarCold else ProgressBarHot,
            trackColor = Color.LightGray,
            strokeCap = StrokeCap.Round
        )
    }
}

@Composable
fun HumiditySection(
    humidity: String
) {
    val value = humidity.toFloatOrNull()?.coerceIn(0f, 100f) ?: 0f
    val targetValue = value / 100f
    val animatedTargetValue by animateFloatAsState(
        targetValue = targetValue,
        animationSpec = tween(4000, easing = LinearOutSlowInEasing),
        label = "humidity animation"
    )
    Column(
        modifier = Modifier
            .size(180.dp)
            .background(shape = RoundedCornerShape(12.dp), color = Color.White)
            .border(width = 2.dp, color = Color.LightGray, shape = RoundedCornerShape(12.dp))
            .padding(all = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Humidity",
            style = Typography.headlineMedium
        )
        CircularProgressIndicator(
            modifier = Modifier.size(92.dp),
            progress = {
                animatedTargetValue
            },
            color = ProgressBarGreen,
            strokeWidth = 16.dp,
            trackColor = ProgressBarBlue,
            strokeCap = StrokeCap.Butt,
        )
        Text(
            text = "$humidity%",
            style = Typography.headlineSmall,
        )
    }
}

@Composable
fun WindSpeedSection(
    windSpeed: String
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.wind)
    )
    Column(
        modifier = Modifier
            .size(180.dp)
            .background(shape = RoundedCornerShape(12.dp), color = Color.White)
            .border(width = 2.dp, color = Color.LightGray, shape = RoundedCornerShape(12.dp))
            .padding(all = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Wind speed",
            style = Typography.headlineMedium
        )
        LottieAnimation(
            modifier = Modifier.size(90.dp),
            composition = composition
        )
        Text(
            text = "$windSpeed m/s",
            style = Typography.headlineSmall,
        )
    }
}