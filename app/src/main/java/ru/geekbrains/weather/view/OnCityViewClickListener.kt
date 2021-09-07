package ru.geekbrains.weather.view

import ru.geekbrains.weather.domain.Weather

interface OnCityViewClickListener {
    fun onCityClick(weather: Weather)
}