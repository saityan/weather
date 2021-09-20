package ru.geekbrains.weather.repository

data class FactDTO (
    val temp : Int,
    val feels_like : Int,
    val condition : String
)