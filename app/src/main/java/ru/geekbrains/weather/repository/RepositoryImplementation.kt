package ru.geekbrains.weather.repository

import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.domain.getRussianCities
import ru.geekbrains.weather.domain.getWorldCities

class RepositoryImplementation : Repository {
    override fun getWeatherFromRemoteSource(): Weather = Weather()

    override fun getWeatherFromLocalSource(): Weather = Weather()

    override fun getWeatherFromLocalStorageRus(): List<Weather> = getWorldCities()

    override fun getWeatherFromLocalStorageWorld(): List<Weather> = getRussianCities()
}