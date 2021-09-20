package ru.geekbrains.weather.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.geekbrains.weather.R
import ru.geekbrains.weather.databinding.ActivityMainBinding
import ru.geekbrains.weather.view.main.MainFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainFragment.newInstance()).commit()
    }
}