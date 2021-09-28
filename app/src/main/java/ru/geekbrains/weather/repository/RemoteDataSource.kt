package ru.geekbrains.weather.repository

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.geekbrains.weather.BuildConfig
import ru.geekbrains.weather.utils.YANDEX_API_URL

class RemoteDataSource {

    private val weatherApi by lazy {
        Retrofit.Builder()
            .baseUrl(YANDEX_API_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build().create(WeatherApi :: class.java)
    }

    fun getWeatherDetails(lat: Double, lon: Double, callback: retrofit2.Callback<WeatherDTO>) {
        weatherApi.getWeather(BuildConfig.WEATHER_API_KEY, lat, lon).enqueue(callback)
    }
}