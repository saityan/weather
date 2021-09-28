package ru.geekbrains.weather.viewmodel

import ru.geekbrains.weather.domain.Weather

sealed class AppState {
    object Loading : AppState()
    data class SuccessMain(val weatherData : List <Weather>) : AppState()
    data class SuccessDetails(val weatherData : Weather) : AppState()
    data class Error(val error : Throwable) : AppState()
}