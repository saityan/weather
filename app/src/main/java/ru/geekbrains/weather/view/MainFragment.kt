package ru.geekbrains.weather.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.geekbrains.weather.databinding.FragmentMainBinding
import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.viewmodel.AppState
import ru.geekbrains.weather.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding : FragmentMainBinding
    get(){
        return _binding!!
    }

    private lateinit var viewModel : MainViewModel

    companion object{
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer<AppState>{
            renderData(it)
        })
        viewModel.getDataFromRemoteSource()
    }

    private fun renderData(appState: AppState) {
        when(appState) {
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                val throwable = appState.error
                Snackbar.make(binding.mainView, "ERROR $throwable", Snackbar.LENGTH_SHORT).show()
            }
            AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                val weather = appState.weatherData
                setData(weather)
                Snackbar.make(binding.mainView, "Success", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun setData(weather: Weather) {
        binding.cityName.text = weather.city.name
        binding.cityCoordinates.text = "latitude ${weather.city.latitude}\n" +
                "longitude ${weather.city.longitude}"
        binding.temperatureValue.text = weather.temperature.toString()
        binding.feelsLikeValue.text = "${weather.feelsLike}"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}