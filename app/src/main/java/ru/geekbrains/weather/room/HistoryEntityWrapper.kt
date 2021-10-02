package ru.geekbrains.weather.room

import androidx.room.*

interface HistoryEntityWrapper {
    @Query("SELECT * FROM HistoryEntity")
    fun getAllHistoryEntities(): List<HistoryEntity>

    @Query("SELECT * FROM HistoryEntity WHERE name LIKE :name")
    fun getHistoryByCity(name: String): List<HistoryEntity>

    @Delete
    fun deleteHistoryEntity(entity: HistoryEntity)

    @Update
    fun updateHistoryEntity (entity: HistoryEntity)

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    fun insertHistoryEntity (entity: HistoryEntity)
}