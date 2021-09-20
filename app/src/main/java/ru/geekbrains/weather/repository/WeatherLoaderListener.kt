package ru.geekbrains.weather.repository

interface WeatherLoaderListener {
    fun onLoaded(weatherDTO : WeatherDTO)
    fun onFailed(throwable : Throwable)
}