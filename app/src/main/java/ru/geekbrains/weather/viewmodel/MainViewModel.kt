package ru.geekbrains.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.weather.repository.RepositoryImplementation
import java.lang.Thread.sleep

class MainViewModel(private val liveDataToObserve:MutableLiveData<AppState> = MutableLiveData(),
                    private val repositoryImplementation: RepositoryImplementation = RepositoryImplementation()
) : ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getWeatherFromLocalSourceWorld() {
        getDataFromLocalSource(true)
    }

    fun getWeatherFromLocalSourceRus() {
        getDataFromLocalSource(false)
    }

    private fun getDataFromLocalSource(isRussian : Boolean) {
        liveDataToObserve.postValue(AppState.Loading)

        Thread {
            sleep(750)
            if(isRussian)
                liveDataToObserve.postValue(AppState.Success(
                    repositoryImplementation.getWeatherFromLocalStorageRus()))
            else
                liveDataToObserve.postValue(AppState.Success(
                    repositoryImplementation.getWeatherFromLocalStorageWorld()))
        }.start()
    }
}