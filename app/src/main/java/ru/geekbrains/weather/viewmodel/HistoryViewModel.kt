package ru.geekbrains.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.weather.MyApp
import ru.geekbrains.weather.repository.LocalRepositoryImplementation

class HistoryViewModel (
    private val historyLiveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val historyRepositoryImplementation: LocalRepositoryImplementation =
        LocalRepositoryImplementation(MyApp.getHistoryDAO())
) : ViewModel() {

    fun getAllHistory() {
        historyLiveDataToObserve.value = AppState.Loading
        historyLiveDataToObserve.value = AppState.SuccessMain(historyRepositoryImplementation.getAllHistory())
        /*Thread {
            historyLiveDataToObserve.postValue(AppState.SuccessMain(historyRepositoryImplementation
            .getAllHistory()))
        }.start()*/
    }

    fun getLiveData() =  historyLiveDataToObserve
}