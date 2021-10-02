package ru.geekbrains.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Response
import ru.geekbrains.weather.MyApp.Companion.getHistoryDAO
import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.repository.DetailsRepositoryImplementation
import ru.geekbrains.weather.repository.LocalRepositoryImplementation
import ru.geekbrains.weather.repository.RemoteDataSource
import ru.geekbrains.weather.repository.WeatherDTO
import ru.geekbrains.weather.utils.convertDTOtoModel
import ru.geekbrains.weather.viewmodel.AppState.*

class DetailsViewModel (
    private val detailsLiveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepositoryImplementation: DetailsRepositoryImplementation =
        DetailsRepositoryImplementation(RemoteDataSource()),
    private val historyRepositoryImplementation: LocalRepositoryImplementation =
        LocalRepositoryImplementation(getHistoryDAO())
) : ViewModel() {

    fun saveWeather(weather: Weather) {
        historyRepositoryImplementation.saveEntity(weather)
    }

    fun getLiveData() = detailsLiveDataToObserve

    fun getWeatherFromRemoteSource(lat: Double, lon: Double) {
        detailsLiveDataToObserve.value = Loading
        detailsRepositoryImplementation.getWeatherDetailsFromRemote(lat, lon, callback)
    }

    private val callback = object : retrofit2.Callback<WeatherDTO> {

        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            val weatherDTO = response.body()
            if (response.isSuccessful && weatherDTO != null) {
                weatherDTO.let {
                    detailsLiveDataToObserve.postValue(SuccessDetails(convertDTOtoModel(weatherDTO)))
                }
            } else {
                detailsLiveDataToObserve.postValue(Error(Throwable()))
            }
        }

        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            detailsLiveDataToObserve.postValue(Error(Throwable()))
        }
    }
}