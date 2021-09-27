package ru.geekbrains.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import ru.geekbrains.weather.repository.DetailsRepositoryImplementation
import ru.geekbrains.weather.repository.RemoteDataSource
import ru.geekbrains.weather.repository.WeatherDTO
import ru.geekbrains.weather.utils.convertDTOtoModel
import ru.geekbrains.weather.viewmodel.AppState.*
import java.io.IOException

class DetailsViewModel (
    private val detailsLiveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepositoryImplementation: DetailsRepositoryImplementation =
        DetailsRepositoryImplementation(RemoteDataSource())
) : ViewModel() {

    fun getLiveData() = detailsLiveDataToObserve

    fun getWeatherFromRemoteSource(requestLink: String) {
        detailsLiveDataToObserve.value = Loading
        detailsRepositoryImplementation.getWeatherDetailsFromRemote(requestLink, callback)
    }

    private val callback = object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            detailsLiveDataToObserve.postValue(Error(Throwable()))
        }

        override fun onResponse(call: Call, response: Response) {
            val serverResponse: String? = response.body?.string()
            if (response.isSuccessful && serverResponse != null) {
                val weatherDTO = Gson().fromJson(serverResponse, WeatherDTO::class.java)
                detailsLiveDataToObserve.postValue(Success(convertDTOtoModel(weatherDTO)))
            } else {
                detailsLiveDataToObserve.postValue(Error(Throwable()))
            }
        }
    }
}