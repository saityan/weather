package ru.geekbrains.weather.repository

import okhttp3.Callback

class DetailsRepositoryImplementation(private val remoteDataSource: RemoteDataSource): DetailsRepository {
    override fun getWeatherDetailsFromRemote(requestLink: String, callback: Callback) {
        remoteDataSource.getWeatherDetails(requestLink, callback)
    }
}