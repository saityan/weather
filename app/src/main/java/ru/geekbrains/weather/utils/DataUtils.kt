package ru.geekbrains.weather.utils

import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.domain.getDefaultCity
import ru.geekbrains.weather.repository.WeatherDTO

fun convertDTOtoModel(weatherDTO: WeatherDTO) : Weather {
    return Weather(getDefaultCity(), weatherDTO.fact.temp, weatherDTO.fact.feels_like,
        weatherDTO.fact.condition)
}