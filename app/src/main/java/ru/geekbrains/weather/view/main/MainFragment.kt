package ru.geekbrains.weather.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.geekbrains.weather.R
import ru.geekbrains.weather.databinding.FragmentMainBinding
import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.view.OnCityViewClickListener
import ru.geekbrains.weather.view.details.DetailsFragment
import ru.geekbrains.weather.viewmodel.AppState
import ru.geekbrains.weather.viewmodel.MainViewModel

class MainFragment : Fragment(), OnCityViewClickListener {

    private var _binding: FragmentMainBinding? = null
    private val binding : FragmentMainBinding
    get() = _binding!!

    private var isDataSourceRus : Boolean = true
    private var adapter = MainFragmentAdapter()

    private lateinit var viewModel : MainViewModel

    companion object{
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            mainFragmentRecyclerView.adapter = adapter
            adapter.setOnCityViewClickListener(this@MainFragment)
            mainFragmentFAB.setOnClickListener {
                isDataSourceRus = !isDataSourceRus
                if (isDataSourceRus) {
                    viewModel.getWeatherFromLocalSourceRus()
                    mainFragmentFAB.setImageResource(R.drawable.ic_russia)
                } else {
                    viewModel.getWeatherFromLocalSourceWorld()
                    mainFragmentFAB.setImageResource(R.drawable.ic_world)
                }
            }
        }

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        with(viewModel) {
            getLiveData()
                .observe(viewLifecycleOwner, Observer<AppState> { appState: AppState ->
                    renderData(appState)
                })
            getWeatherFromLocalSourceRus()
        }
    }

    private fun renderData(appState: AppState) {
        when(appState) {
            is AppState.Error -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                val throwable = appState.error
                Snackbar.make(binding.root, "ERROR $throwable", Snackbar.LENGTH_SHORT).show()
            }
            AppState.Loading -> {
                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                val weather = appState.weatherData
                adapter.setWeather(weather)
                binding.root.showSnackbarWithoutAction(binding.root, R.string.snackbar, Snackbar.LENGTH_SHORT)
            }
        }
    }

    fun View.showSnackbarWithoutAction(view : View, stringId : Int, length : Int) {
        Snackbar.make(view, getString(stringId), length).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCityClick(weather: Weather) {
        val bundle = Bundle()
        bundle.putParcelable(DetailsFragment.BUNDLE_WEATHER_KEY, weather)
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, DetailsFragment.newInstance(bundle))
            .addToBackStack("")
            .commit()
    }
}