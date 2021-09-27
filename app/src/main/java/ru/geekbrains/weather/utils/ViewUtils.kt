package ru.geekbrains.weather.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnack (text: String, actionText: String, action:(View) -> Unit) {
    Snackbar.make(this, text, Snackbar.LENGTH_SHORT).setAction(actionText, action).show()
}