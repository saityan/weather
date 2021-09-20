package ru.geekbrains.weather.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.geekbrains.weather.databinding.FragmentDetailsBinding
import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.repository.WeatherDTO
import ru.geekbrains.weather.repository.WeatherLoader
import ru.geekbrains.weather.repository.WeatherLoaderListener

class DetailsFragment : Fragment(), WeatherLoaderListener {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    companion object {
        fun newInstance(bundle: Bundle) : DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
        const val BUNDLE_WEATHER_KEY = "key"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    val localWeather : Weather by lazy {
        arguments?.getParcelable(BUNDLE_WEATHER_KEY) ?: Weather()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        WeatherLoader(this, localWeather.city.lat, localWeather.city.lon).loadWeather()
    }

    private fun showWeather(weatherDTO : WeatherDTO) {
        with(binding) {
            with(weatherDTO) {
                cityName.text = localWeather.city.name
                cityCoordinates.text = "latitude ${localWeather.city.lat}\n" +
                        "longitude ${localWeather.city.lon}"
                temperatureValue.text = fact.temp.toString()
                feelsLikeValue.text = "${fact.feels_like}"
                weatherCondition.text = fact.condition
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onLoaded(weatherDTO: WeatherDTO) {
        showWeather(weatherDTO)
    }

    override fun onFailed(throwable: Throwable) {
        TODO("Not yet implemented")
    }
}
