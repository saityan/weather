package ru.geekbrains.weather.repository

import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.room.HistoryDAO
import ru.geekbrains.weather.utils.convertHistoryEntityToWeather
import ru.geekbrains.weather.utils.convertWeatherToHistoryEntity

class LocalRepositoryImplementation(private val localDataSource: HistoryDAO) : LocalRepository {
    override fun getAllHistory(): List<Weather> {
        return convertHistoryEntityToWeather(localDataSource.getAllHistoryEntities())
    }

    override fun saveEntity(weather: Weather) {
        Thread {
            localDataSource.insertHistoryEntity(convertWeatherToHistoryEntity(weather))
        }.start()
    }
}