package ru.geekbrains.weather.domain

data class Weather (val city: City = getDefaultCity(),
                    val temperature : Int = -1,
                    val feelsLike : Int = -4) {
}
private fun getDefaultCity() = City("Москва", 55.56, 37.38)