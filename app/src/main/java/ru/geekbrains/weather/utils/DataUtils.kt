package ru.geekbrains.weather.utils

import ru.geekbrains.weather.domain.City
import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.domain.getDefaultCity
import ru.geekbrains.weather.repository.WeatherDTO
import ru.geekbrains.weather.room.HistoryEntity

fun convertDTOtoModel (weatherDTO: WeatherDTO) : Weather {
    return Weather(getDefaultCity(), weatherDTO.fact.temp, weatherDTO.fact.feels_like,
        weatherDTO.fact.condition)
}

fun convertHistoryEntityToWeather (entityList: List<HistoryEntity>): List<Weather> {
    return entityList.map {
        Weather(City(it.name, 0.0, 0.0), it.temperature, 0, it.condition)
    }
}

fun convertWeatherToHistoryEntity(weather: Weather): HistoryEntity {
    return HistoryEntity(0, weather.city.name, weather.temp, weather.condition)
}