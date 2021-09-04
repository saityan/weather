package ru.geekbrains.weather.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.weather.repository.RepositoryImplementation
import java.lang.IllegalStateException
import java.lang.Thread.sleep

class MainViewModel(private val liveDataToObserve:MutableLiveData<AppState> = MutableLiveData(),
                    private val repositoryImplementation: RepositoryImplementation = RepositoryImplementation()
) : ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getDataFromRemoteSource() {
        liveDataToObserve.postValue(AppState.Loading)

        Thread {
            sleep(2000)
            val r = (0..10).random()
            if(r > 5)
            liveDataToObserve.postValue(AppState.Success(
                repositoryImplementation.getWeatherFromRemoteSource()))
            else
                liveDataToObserve.postValue(AppState.Error(IllegalStateException()))
        }.start()
    }
}