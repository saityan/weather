package ru.geekbrains.weather.repository

import retrofit2.Callback

interface DetailsRepository {
    fun getWeatherDetailsFromRemote(lat: Double, lon: Double, callback: Callback<WeatherDTO>)
}