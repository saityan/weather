package ru.geekbrains.weather.view.main

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.geekbrains.weather.R
import ru.geekbrains.weather.databinding.FragmentMainBinding
import ru.geekbrains.weather.domain.City
import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.view.OnCityViewClickListener
import ru.geekbrains.weather.view.details.DetailsFragment
import ru.geekbrains.weather.viewmodel.AppState
import ru.geekbrains.weather.viewmodel.MainViewModel

class MainFragment : Fragment(), OnCityViewClickListener {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding!!

    private var isDataSourceRus: Boolean = true
    private var adapter = MainFragmentAdapter()

    private lateinit var viewModel: MainViewModel

    private val REQUEST_CODE = 777
    private val REFRESH_PERIOD = 6000L
    private val MINIMAL_DISTANCE = 100f

    private val onLocationChangeListener = LocationListener {
        getAddressAsync(context, it)
    }

    private fun getAddressAsync(context: Context?, location: Location) {
        val geocoder = Geocoder(context)
        val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        showAddressDialog(address[0].getAddressLine(0), location)
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    private fun showAddressDialog(address: String, location: Location) {
        activity?.let {
            androidx.appcompat.app.AlertDialog.Builder(it)
                .setTitle(R.string.dialog_address_title)
                .setMessage(address)
                .setPositiveButton(R.string.dialog_address_get_weather) { dialog, which ->
                    onCityClick(
                        Weather(
                            City(
                                address,
                                location.latitude,
                                location.longitude
                            )
                        )
                    )
                }
                .setNegativeButton(R.string.dialog_button_close) { dialog, which ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
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
            mainFragmentFABLocation.setOnClickListener {
                checkLocationPermission()
            }
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
        when (appState) {
            is AppState.Error -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                val throwable = appState.error
                Snackbar.make(binding.root, "ERROR $throwable", Snackbar.LENGTH_SHORT).show()
            }
            AppState.Loading -> {
                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.SuccessMain -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                val weather = appState.weatherData
                adapter.setWeather(weather)
                binding.root.showSnackbarWithoutAction(
                    binding.root,
                    R.string.snackbar,
                    Snackbar.LENGTH_SHORT
                )
            }
        }
    }

    fun View.showSnackbarWithoutAction(view: View, stringId: Int, length: Int) {
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

    private fun checkLocationPermission() {
        context?.let {
            if (ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                getLocation()
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                showRatio()
            } else {
                locationRequestPermission()
            }
        }
    }

    private fun getLocation() {
        activity?.let {
            if (ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val locationManager =
                    it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                    provider?.let {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            REFRESH_PERIOD, MINIMAL_DISTANCE, onLocationChangeListener
                        )
                    }
                } else {
                    val location =
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    location?.let {

                    }
                }
            } else {
                showRatio()
            }
        }
    }

    private fun locationRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
    }

    private fun showRatio() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_rationale_title)
            .setMessage(R.string.dialog_rationale_message)
            .setPositiveButton(R.string.dialog_rationale_give_access) { dialog, which ->
                locationRequestPermission()
            }
            .setNegativeButton(R.string.dialog_rationale_decline) { dialog, which ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onRequestPermissionsResult(
        REQUEST_CODE: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (REQUEST_CODE) {
            this.REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    getLocation()
                } else {
                    context?.let {
                        showRatio()
                    }
                }
            }
        }
        super.onRequestPermissionsResult(REQUEST_CODE, permissions, grantResults)
    }
}