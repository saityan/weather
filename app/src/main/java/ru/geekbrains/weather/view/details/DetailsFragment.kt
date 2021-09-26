package ru.geekbrains.weather.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import ru.geekbrains.weather.databinding.FragmentDetailsBinding
import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.repository.WeatherDTO
import ru.geekbrains.weather.repository.WeatherLoader
import ru.geekbrains.weather.repository.WeatherLoaderListener

class DetailsFragment : Fragment(), WeatherLoaderListener {

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

    val localWeather : Weather by lazy {
        arguments?.getParcelable(BUNDLE_WEATHER_KEY) ?: Weather()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //WeatherLoader(this, localWeather.city.lat, localWeather.city.lon).loadWeather()

        val intent = Intent(requireActivity(), DetailsService :: class.java)
        intent.putExtra(LATITUDE_EXTRA, localWeather.city.lat)
        intent.putExtra(LONGITUDE_EXTRA, localWeather.city.lon)
        requireActivity().startService(intent)
        LocalBroadcastManager.getInstance(requireActivity())
            .registerReceiver(receiver, IntentFilter(DETAILS_INTENT_FILTER))
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

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val weatherDTO = intent?.getParcelableExtra<WeatherDTO>(DETAILS_LOAD_RESULT_EXTRA)
            if (weatherDTO != null) { showWeather(weatherDTO) }
            else {/*ERROR*/}
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(receiver)
    }

    override fun onLoaded(weatherDTO: WeatherDTO) {
        showWeather(weatherDTO)
    }

    override fun onFailed(throwable: Throwable) {
        Snackbar.make(binding.root, "CONNECTION ERROR", Snackbar.LENGTH_SHORT).show()
    }
}
