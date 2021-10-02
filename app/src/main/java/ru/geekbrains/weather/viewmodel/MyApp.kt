package ru.geekbrains.weather.viewmodel

import android.app.Application
import androidx.room.Room
import ru.geekbrains.weather.room.HistoryDataBase
import ru.geekbrains.weather.room.HistoryEntityWrapper
import java.lang.IllegalStateException

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        private var appInstance: MyApp? = null
        private var db: HistoryDataBase? = null
        private const val DB_NAME = "HistoryDataBase.db"

        fun getHistoryWrapper(): HistoryEntityWrapper {
            if (db == null) {
                if(appInstance != null) {
                    db = Room.databaseBuilder(appInstance!!.applicationContext, HistoryDataBase :: class.java, DB_NAME)
                        .build()
                } else {
                    throw IllegalStateException("appInstance == null")
                }
            }
            return db!!.getHistoryWrapper()
        }
    }
}