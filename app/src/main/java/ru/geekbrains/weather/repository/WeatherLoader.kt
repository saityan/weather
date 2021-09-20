package ru.geekbrains.weather.repository

import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(val listener : WeatherLoaderListener, val lat : Double, val lon : Double) {
    fun loadWeather() {
        val url = URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}")

        Thread {
            val urlConnection = url.openConnection() as HttpsURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.readTimeout = 5000
            urlConnection.addRequestProperty("X-Yandex-API-Key", "7cce9b65-e284-4439-8c79-968b01577355")
            val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val weatherDTO = Gson().fromJson(reader, WeatherDTO :: class.java)
            val handler = Handler(Looper.getMainLooper())

            handler.post{
                listener.onLoaded(weatherDTO)
            }

            urlConnection.disconnect()
        }.start()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines (reader : BufferedReader) : String {
        return reader.lines().collect(Collectors.joining("\n"))
    }
}