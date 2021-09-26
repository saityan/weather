package ru.geekbrains.weather.repository

import okhttp3.Callback

interface DetailsRepository {
    fun getWeatherDetailsFromRemote(requestLink : String, callback: Callback)
}