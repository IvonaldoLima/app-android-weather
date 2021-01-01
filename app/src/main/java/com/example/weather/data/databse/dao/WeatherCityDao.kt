package com.example.weather.data.databse.dao

import androidx.room.*
import com.example.weather.data.model.CityDatabase

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