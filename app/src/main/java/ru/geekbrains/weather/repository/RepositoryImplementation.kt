package ru.geekbrains.weather.repository

import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.domain.getRussianCities
import ru.geekbrains.weather.domain.getWorldCities

class RepositoryImplementation : Repository {
    override fun getWeatherFromRemoteSource(): Weather {
        return Weather()
    }

    override fun getWeatherFromLocalSource(): Weather {
        return Weather()
    }

    override fun getWeatherFromLocalStorageRus(): List<Weather> {
        return getWorldCities()
    }

    override fun getWeatherFromLocalStorageWorld(): List<Weather> {
        return getRussianCities()
    }
}