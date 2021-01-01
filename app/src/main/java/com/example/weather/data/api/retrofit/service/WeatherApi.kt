package com.example.weather.data.api.retrofit.service

import com.example.weather.data.model.WeatherBundle
import com.example.weather.data.model.WeatherCity
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApi {
    //    http://api.openweathermap.org/data/2.5/weather?q=London,uk&APPID=23af5614bbb8fa0d86da0d819c3ebe7b

    @GET("2.5/find")
    suspend fun findWeather(
        @Query("q") city: String,
        @Query("units") units: String,
        @Query("lang") language: String,
        @Query("APPID") appId: String
    ): Response<WeatherBundle>

    @GET("2.5/weather")
    suspend fun getWeatherByCityID(
        @Query("id") cityId: Int,
        @Query("units") units: String,
        @Query("lang") language: String,
        @Query("APPID") appId: String
    ): Response<WeatherCity>
}