package ru.geekbrains.weather.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.geekbrains.weather.databinding.FragmentDetailsBinding
import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.viewmodel.AppState
import ru.geekbrains.weather.viewmodel.DetailsViewModel

class DetailsFragment : Fragment(){

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
        viewModel.getWeatherFromRemoteSource("https://api.weather.yandex.ru/v2/informers?lat=" +
                "${localWeather.city.lat}&lon=${localWeather.city.lon}")
    }

    private fun renderData(appState: AppState) {
        when(appState) {
            is AppState.Error -> {
                val throwable = appState.error
                Snackbar.make(binding.root, "ERROR $throwable", Snackbar.LENGTH_SHORT).show()
            }
            AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
                binding.mainView.visibility = View.INVISIBLE
            }
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.INVISIBLE
                binding.mainView.visibility = View.VISIBLE
                val weather = appState.weatherData
                showWeather(weather[0])
                Snackbar.make(binding.root, "SUCCESS", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun showWeather(weather: Weather) {
        with(binding) {
            with(weather) {
                cityName.text = localWeather.city.name
                cityCoordinates.text = "latitude ${localWeather.city.lat}\n" +
                        "longitude ${localWeather.city.lon}"
                temperatureValue.text = temp.toString()
                feelsLikeValue.text = "$feels_like"
                weatherCondition.text = condition
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}