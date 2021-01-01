package com.example.weather.data.api

import com.example.weather.data.model.WeatherBundle
import com.example.weather.data.api.retrofit.RetrofitManager
import com.example.weather.data.api.retrofit.service.WeatherApi
import com.example.weather.data.model.WeatherCity

class WeatherCoroutineService() : BaseService() {

    override fun parseCustomError(
        responseMessage: String,
        responseCode: Int,
        errorBodyJson: String
    ): Exception {
        TODO("Not yet implemented")
    }

    private val appIdOpenWeather: String = "23af5614bbb8fa0d86da0d819c3ebe7b"
    private val weatherApi: WeatherApi = RetrofitManager().noticiaApi

    //http://api.openweathermap.org/data/2.5/weather?q=Recife,br&APPID=23af5614bbb8fa0d86da0d819c3ebe7b
    suspend fun searchCityWeather(cityName: String, units: String, language: String): Resource<WeatherBundle> {
        return apiCallResource(call = {
            weatherApi.findWeather(
                cityName,
                units,
                language,
                appIdOpenWeather
            )
        })
    }

    suspend fun searchCityById(cityID: Int, units: String, language: String): Resource<WeatherCity> {
        return apiCallResource(call = {
            weatherApi.getWeatherByCityID(
                cityID,
                units,
                language,
                appIdOpenWeather
            )
        })
    }
}