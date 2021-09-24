package ru.geekbrains.weather.view.details

import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import ru.geekbrains.weather.repository.WeatherDTO
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

const val LATITUDE_EXTRA = "Latitude"
const val LONGITUDE_EXTRA = "Longitude"
const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"

class DetailsService (name: String = "details") : IntentService(name) {
    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            val lat = it.getDoubleExtra(LATITUDE_EXTRA, -1.0)
            val lon = it.getDoubleExtra(LONGITUDE_EXTRA, -1.0)
            loadWeather(lat, lon)
        }
    }

    fun loadWeather(lat: Double, lon: Double) {
        val url = URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}")

        Thread {
            val urlConnection = url.openConnection() as HttpsURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.readTimeout = 5000
            urlConnection.addRequestProperty("X-Yandex-API-Key", "7cce9b65-e284-4439-8c79-968b01577355")
            val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val weatherDTO = Gson().fromJson(reader, WeatherDTO :: class.java)
            val handler = Handler(Looper.getMainLooper())
            urlConnection.disconnect()

            val sendIntent = Intent(DETAILS_INTENT_FILTER)
            sendIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, weatherDTO)
            LocalBroadcastManager.getInstance(this).sendBroadcast(sendIntent)
        }.start()
    }
}