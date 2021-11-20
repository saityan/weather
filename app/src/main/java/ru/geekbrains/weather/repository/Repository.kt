package ru.geekbrains.weather.repository

import ru.geekbrains.weather.domain.Weather

interface Repository {
    fun getWeatherFromRemoteSource(): Weather
    fun getWeatherFromLocalSource(): Weather
    fun getWeatherFromLocalStorageWorld(): List<Weather>
    fun getWeatherFromLocalStorageRus(): List<Weather>
}