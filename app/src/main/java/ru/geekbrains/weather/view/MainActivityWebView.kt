package ru.geekbrains.weather.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import ru.geekbrains.weather.R
import ru.geekbrains.weather.databinding.ActivityMainWebviewBinding
import ru.geekbrains.weather.databinding.FragmentDetailsBinding
import ru.geekbrains.weather.view.main.MainFragment
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class MainActivityWebView : AppCompatActivity() {
    lateinit var binding: ActivityMainWebviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webViewOk.setOnClickListener {
            showURL(binding.webViewEditText.text.toString())
        }
    }

    private fun showURL (urlString : String) {
        val url = URL(urlString)

        Thread {
            val urlConnection = url.openConnection() as HttpsURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.readTimeout = 5000
            val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val result = getLines(reader)

            val handler = Handler(Looper.getMainLooper())
            handler.post {
                binding.webView.loadDataWithBaseURL(null, result, "text/html; charset=utf-8", "utf-8", null)
            }

            urlConnection.disconnect()

        }.start()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines (reader : BufferedReader) : String {
        return reader.lines().collect(Collectors.joining("\n"))
    }
}