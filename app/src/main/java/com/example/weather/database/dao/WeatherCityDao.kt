package com.example.weather.database.dao

import androidx.room.*
import com.example.weather.model.CityDatabase

@Dao
interface WeatherCityDao {

    @Query("SELECT * FROM weather_city")
    fun getAll(): MutableList<CityDatabase>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cityDatabase: CityDatabase)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg cityDatabase: CityDatabase)

    @Delete
    fun delete(cityDatabase: CityDatabase): Int
}