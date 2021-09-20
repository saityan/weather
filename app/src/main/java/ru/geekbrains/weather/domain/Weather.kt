package ru.geekbrains.weather.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather (val city: City = getDefaultCity(),
                    val temp : Int = -1,
                    val feels_like : Int = -4) : Parcelable

private fun getDefaultCity() = City("Москва", 55.56, 37.38)

fun getWorldCities(): List<Weather> = listOf(
        Weather(City("Лондон", 51.51, -0.13), 1, 2),
        Weather(City("Токио", 35.69, 139.69), 3, 4),
        Weather(City("Париж", 48.85, 2.35), 5, 6),
        Weather(City("Берлин", 52.52, 13.40), 7, 8),
        Weather(City("Рим", 41.90, 12.50), 9, 10),
        Weather(City("Минск", 53.90, 27.56), 11, 12),
        Weather(City("Стамбул", 41.01, 28.98), 13, 14),
        Weather(City("Вашингтон", 38.91, -77.04), 15, 16),
        Weather(City("Киев", 50.45, 30.52), 17, 18),
        Weather(City("Пекин", 39.90, 116.41), 19, 20)
    )

fun getRussianCities(): List<Weather> = listOf(
        Weather(City("Москва", 55.76, 37.62), 1, 2),
        Weather(City("Санкт-Петербург", 59.93, 30.33), 3, 3),
        Weather(City("Новосибирск", 55.01, 82.94), 5, 6),
        Weather(City("Екатеринбург", 56.84, 60.61), 7, 8),
        Weather(City("Нижний Новгород", 56.30, 43.94), 9, 10),
        Weather(City("Казань", 55.83, 49.07), 11, 12),
        Weather(City("Челябинск", 55.16, 61.44), 13, 14),
        Weather(City("Омск", 54.99, 73.32), 15, 16),
        Weather(City("Ростов-на-Дону", 47.24, 39.70), 17, 18),
        Weather(City("Уфа", 54.74, 55.97), 19, 20)
)