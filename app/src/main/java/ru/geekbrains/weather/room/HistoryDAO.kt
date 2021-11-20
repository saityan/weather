package ru.geekbrains.weather.room

import android.database.Cursor
import androidx.room.*

const val ID = "id"
const val NAME = "name"
const val TEMPERATURE = "temperature"

@Dao
interface HistoryDAO {
    @Query("SELECT * FROM HistoryEntity")
    fun getAllHistoryEntities(): List<HistoryEntity>

    @Query("SELECT * FROM HistoryEntity WHERE name LIKE :name")
    fun getHistoryByCity(name: String): List<HistoryEntity>

    @Delete
    fun deleteHistoryEntity(entity: HistoryEntity)

    @Query("DELETE FROM HistoryEntity WHERE id=:id")
    fun deleteHistoryEntity(id: Long)

    @Update
    fun updateHistoryEntity(entity: HistoryEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertHistoryEntity(entity: HistoryEntity)

    @Query("SELECT id, name, temperature FROM HistoryEntity")
    fun getHistoryPointer(): Cursor

    @Query("SELECT id, name, temperature FROM HistoryEntity WHERE id=:id")
    fun getHistoryPointer(id: Long): Cursor
}