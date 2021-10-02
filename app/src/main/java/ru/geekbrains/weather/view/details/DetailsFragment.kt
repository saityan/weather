package ru.geekbrains.weather.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.snackbar.Snackbar
import ru.geekbrains.weather.databinding.FragmentDetailsBinding
import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.utils.showSnack
import ru.geekbrains.weather.viewmodel.AppState
import ru.geekbrains.weather.viewmodel.DetailsViewModel

class DetailsFragment : Fragment() {

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel :: class.java)
    }

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
    get() = _binding!!

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
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val localWeather : Weather by lazy {
        arguments?.getParcelable(BUNDLE_WEATHER_KEY) ?: Weather()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner, {
            renderData(it)
        })
        getWeather()
    }

    private fun renderData(appState: AppState) {
        when(appState) {
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.INVISIBLE
                binding.mainView.visibility = View.VISIBLE
                val throwable = appState.error
                binding.root.showSnack("CONNECTION ERROR $throwable", "RELOAD") {
                    getWeather()
                }
            }
            AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
                binding.mainView.visibility = View.INVISIBLE
            }
            is AppState.SuccessDetails -> {
                binding.loadingLayout.visibility = View.INVISIBLE
                binding.mainView.visibility = View.VISIBLE
                val weather = appState.weatherData
                showWeather(weather)
                Snackbar.make(binding.root, "SUCCESS", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun showWeather(weather: Weather) {
        viewModel.saveWeather(Weather(localWeather.city, weather.temp, weather.feels_like, weather.condition))
        with(binding) {
            with(weather) {
                cityName.text = localWeather.city.name
                cityCoordinates.text = "latitude ${localWeather.city.lat}\n" +
                        "longitude ${localWeather.city.lon}"
                temperatureValue.text = temp.toString()
                feelsLikeValue.text = "$feels_like"
                weatherCondition.text = condition
                binding.imageViewHeader.load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
            }
        }
    }

    private fun getWeather() {
        viewModel.getWeatherFromRemoteSource(localWeather.city.lat, localWeather.city.lon)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
