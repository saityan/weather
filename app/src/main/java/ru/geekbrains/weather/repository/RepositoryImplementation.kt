package ru.geekbrains.weather.repository

import ru.geekbrains.weather.domain.Weather

class RepositoryImplementation : Repository {
    override fun getWeatherFromRemoteSource(): Weather {
        return Weather()
    }

    override fun getWeatherFromLocalSource(): Weather {
        return Weather()
    }
}