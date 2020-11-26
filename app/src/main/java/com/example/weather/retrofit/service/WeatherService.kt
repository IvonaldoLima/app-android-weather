package com.example.weather.retrofit.service

import com.example.weather.model.WeatherBundle
import com.example.weather.model.WeatherCity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {
    //    http://api.openweathermap.org/data/2.5/weather?q=London,uk&APPID=23af5614bbb8fa0d86da0d819c3ebe7b
    @GET("2.5/find")
    fun findWeatherByCity(
        @Query("q") city: String,
        @Query("units") units: String,
        @Query("lang") language: String,
        @Query("APPID") appId: String
    ): Call<WeatherBundle>

    @GET("2.5/weather")
    fun getWeather(
        @Query("id") cityId: String,
        @Query("units") units: String,
        @Query("lang") language: String,
        @Query("APPID") appId: String
    ): Call<WeatherCity>
}