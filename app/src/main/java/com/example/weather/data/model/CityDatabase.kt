package com.example.weather.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_city")
class CityDatabase(
    @PrimaryKey
    val id: Int,
    val city: String?,
    val temperature: Float?,
    val temperature_min: Float?,
    val temperature_max: Float?,
    val description: String?,
    var favorite: Boolean = false,
    val icon: String?,
    val temperatureUnit: String?,
    val language: String
)