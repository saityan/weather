package ru.geekbrains.weather.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(val name : String, val latitude : Double, val longitude : Double) : Parcelable