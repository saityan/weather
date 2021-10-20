package ru.geekbrains.weather.view

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import ru.geekbrains.weather.MyApp.Companion.getHistoryDAO
import ru.geekbrains.weather.R
import ru.geekbrains.weather.databinding.ActivityMainBinding
import ru.geekbrains.weather.maps.MapsFragment
import ru.geekbrains.weather.view.contentprovider.ContentProviderFragment
import ru.geekbrains.weather.view.history.HistoryFragment
import ru.geekbrains.weather.view.main.MainFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainFragment.newInstance()).commit()

        getHistoryDAO()

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful)
                Log.d("myLogs", it.result.toString())
        }
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
}