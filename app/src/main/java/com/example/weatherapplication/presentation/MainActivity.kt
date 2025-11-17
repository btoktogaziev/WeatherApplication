package com.example.weatherapplication.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.weatherapplication.R
import com.example.weatherapplication.presentation.ui.screens.Screens
import com.example.weatherapplication.presentation.ui.screens.current.CurrentWeatherScreen
import com.example.weatherapplication.presentation.ui.screens.forecast.ForecastWeatherScreen
import com.example.weatherapplication.presentation.ui.theme.WeatherApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherApplicationTheme {
                WeatherApp()
            }
        }
    }
}

@Composable
private fun WeatherApp() {
    val navController = rememberNavController()
    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_day),
            contentDescription = "bg",
            contentScale = ContentScale.Crop
        )
        NavHost(
            navController = navController,
            startDestination = Screens.CurrentWeatherScreen.route,
        ) {
            composable(
                route = Screens.CurrentWeatherScreen.route,
            ) {
                CurrentWeatherScreen(
                    toForecastScreen = { cityName ->
                        navController.navigate(Screens.ForecastWeatherScreen.createRoute(cityName))
                    }
                )
            }
            composable(
                route = "${Screens.ForecastWeatherScreen.route}/{cityName}",
                arguments = listOf(navArgument("cityName") {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                val cityName = backStackEntry.arguments?.getString("cityName") ?: "Not found"
                ForecastWeatherScreen(
                    cityName = cityName
                )
            }

        }
    }
}