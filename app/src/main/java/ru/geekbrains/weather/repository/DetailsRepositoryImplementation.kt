package ru.geekbrains.weather.repository

import retrofit2.Callback

class DetailsRepositoryImplementation(private val remoteDataSource: RemoteDataSource) :
    DetailsRepository {
    override fun getWeatherDetailsFromRemote(
        lat: Double,
        lon: Double,
        callback: Callback<WeatherDTO>
    ) {
        remoteDataSource.getWeatherDetails(lat, lon, callback)
    }
}