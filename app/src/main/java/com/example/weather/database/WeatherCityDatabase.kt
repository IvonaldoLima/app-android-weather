package com.example.weather.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weather.database.dao.WeatherCityDao
import com.example.weather.model.CityDatabase

@Database(entities = [CityDatabase::class], version = 1)
abstract class WeatherCityDatabase : RoomDatabase() {

    abstract fun weatherCityDao(): WeatherCityDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherCityDatabase? = null

        fun getInsanceDatabase(context: Context): WeatherCityDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherCityDatabase::class.java,
                    "weather_database"
                )
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}