package ru.geekbrains.weather

import android.app.Application
import androidx.room.Room
import ru.geekbrains.weather.room.HistoryDAO
import ru.geekbrains.weather.room.HistoryDataBase

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        private var appInstance: MyApp? = null
        private var db: HistoryDataBase? = null
        private const val DB_NAME = "HistoryDataBase.db"

        fun getHistoryDAO(): HistoryDAO {
            if (db == null) {
                synchronized(HistoryDataBase::class.java) {
                    if (db == null) {
                        if (appInstance != null) {
                            db = Room.databaseBuilder(
                                appInstance!!.applicationContext,
                                HistoryDataBase::class.java,
                                DB_NAME
                            ).build()
                        } else {
                            throw IllegalStateException("appInstance == null")
                        }
                    }
                }
            }
            return db!!.getHistoryDAO()
        }
    }
}