package com.example.weather.retrofit

import com.example.weather.retrofit.service.WeatherService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "http://api.openweathermap.org/data/"
//http://api.openweathermap.org/data/2.5/weather?q=London,uk&APPID=23af5614bbb8fa0d86da0d819c3ebe7b
class RetrofitManager {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val noticiaService: WeatherService by lazy {
        retrofit.create(WeatherService::class.java)
    }
}