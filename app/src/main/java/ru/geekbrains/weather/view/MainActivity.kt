package ru.geekbrains.weather.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import ru.geekbrains.weather.MyApp.Companion.getHistoryDAO
import ru.geekbrains.weather.R
import ru.geekbrains.weather.databinding.ActivityMainBinding
import ru.geekbrains.weather.maps.MapsFragment
import ru.geekbrains.weather.view.contentprovider.ContentProviderFragment
import ru.geekbrains.weather.view.history.HistoryFragment
import ru.geekbrains.weather.view.main.MainFragment

class MainActivity : AppCompatActivity() {

    companion object {
        private const val NOTIFICATION_FIRST = 1
        private const val NOTIFICATION_SECOND = 2
        private const val CHANNEL_ID1 = "channel_id1"
        private const val CHANNEL_ID2 = "channel_id2"
    }

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainFragment.newInstance()).commit()
        getHistoryDAO()
        pushNotificationsTest()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {

            R.id.action_open_fragment_history -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HistoryFragment.newInstance())
                    .addToBackStack("")
                    .commit()
                true
            }

            R.id.action_open_fragment_content_provider -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ContentProviderFragment.newInstance())
                    .addToBackStack("")
                    .commit()
                true
            }

            R.id.action_open_fragment_menu_google_maps -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, MapsFragment.newInstance())
                    .addToBackStack("")
                    .commit()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun pushNotificationsTest() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilderOne = NotificationCompat.Builder(this, CHANNEL_ID1).apply {
            setSmallIcon(R.drawable.ic_map_pin)
            setContentTitle("Header for the $CHANNEL_ID1")
            setContentText("Text for the $CHANNEL_ID1")
            priority = NotificationCompat.PRIORITY_DEFAULT
        }

        val notificationBuilderTwo = NotificationCompat.Builder(this, CHANNEL_ID2).apply {
            setSmallIcon(R.drawable.ic_map_marker)
            setContentTitle("Header for the $CHANNEL_ID2")
            setContentText("Text for the $CHANNEL_ID2")
            priority = NotificationCompat.PRIORITY_DEFAULT
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nameFirst = "Name $CHANNEL_ID1"
            val descFirst = "Description $CHANNEL_ID1"
            val importanceFirst = NotificationManager.IMPORTANCE_DEFAULT
            val channelFirst = NotificationChannel(CHANNEL_ID1, nameFirst, importanceFirst).apply {
                description = descFirst
            }
            notificationManager.createNotificationChannel(channelFirst)
        }
        notificationManager.notify(NOTIFICATION_FIRST, notificationBuilderOne.build())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nameSecond = "Name $CHANNEL_ID2"
            val descSecond = "Description $CHANNEL_ID2"
            val importanceSecond = NotificationManager.IMPORTANCE_LOW
            val channelSecond =
                NotificationChannel(CHANNEL_ID2, nameSecond, importanceSecond).apply {
                    description = descSecond
                }
            notificationManager.createNotificationChannel(channelSecond)
        }
        notificationManager.notify(NOTIFICATION_SECOND, notificationBuilderTwo.build())
    }
}