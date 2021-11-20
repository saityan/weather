package ru.geekbrains.weather.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.geekbrains.weather.R
import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.view.OnCityViewClickListener

class MainFragmentAdapter : RecyclerView.Adapter<MainFragmentAdapter.MainFragmentViewHolder>() {

    private var weatherData: List<Weather> = listOf()
    private lateinit var listener: OnCityViewClickListener

    fun setWeather(data: List<Weather>) {
        weatherData = data
        notifyDataSetChanged()
    }

    fun setOnCityViewClickListener(onCityViewClickListener: OnCityViewClickListener) {
        listener = onCityViewClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainFragmentViewHolder {
        return MainFragmentViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_main_recycler_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainFragmentViewHolder, position: Int) =
        holder.render(weatherData[position])

    override fun getItemCount(): Int = weatherData.size

    inner class MainFragmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun render(weather: Weather) {
            itemView.findViewById<TextView>(
                R.id.mainFragmentRecyclerItemTextView
            ).text = weather.city.name
            itemView.setOnClickListener { listener.onCityClick(weather) }
        }
    }
}