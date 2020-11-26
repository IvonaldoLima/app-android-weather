package com.example.weather.model

import com.google.gson.annotations.SerializedName

data class WeatherBundle(val count: Int,
                         @SerializedName("list")
                         val weatherCity: ArrayList<WeatherCity>)


data class WeatherCity(
    val id: Int,
    val name: String,
    val main: Main,
    var favorite: Boolean,
    @SerializedName("weather")
    val weather: ArrayList<Weather>
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String,
)


data class Main(
                val id: Int,
                val temp:Float,
                @SerializedName("temp_min")
                val tempMin: Float,
                @SerializedName("temp_max")
                val tempMax: Float )
