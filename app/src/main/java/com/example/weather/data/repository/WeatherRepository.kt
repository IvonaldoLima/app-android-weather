package com.example.weather.data.repository

import com.example.weather.data.api.Resource
import com.example.weather.data.api.Status
import com.example.weather.data.api.WeatherCoroutineService
import com.example.weather.data.databse.dao.WeatherCityDao
import com.example.weather.data.model.CityDatabase
import com.example.weather.data.model.WeatherCity
import com.example.weather.data.model.mapper.CityDatabaseMapper
import com.example.weather.ui.Constants
import com.example.weather.util.UtilSharedPreferences


class WeatherRepository(
    private val weatherCityDao: WeatherCityDao,
    private val service: WeatherCoroutineService = WeatherCoroutineService(),
    utilSharedPreferences: UtilSharedPreferences
) {

    private var temperatureUnitPreference =
        utilSharedPreferences.getValue(Constants.preferenceTemperatureUnit).toString()
    private var languagePreference =
        utilSharedPreferences.getValue(Constants.preferenceLanguage).toString()

    suspend fun searchWeatherCity(cityName: String): Resource<MutableList<CityDatabase>> {
        var resource =
            service.searchCityWeather(cityName, temperatureUnitPreference, languagePreference)
        var listWeather = mutableListOf<CityDatabase>()

        return if (resource.status == Status.SUCCESS) {
            resource.data!!.weatherCity.forEach {
                val cityDatabase = mapWeatherCityToCityDatabase(it)
                listWeather.add(cityDatabase)
            }
            Resource.success(listWeather)
        } else {
            Resource.error(msg = "Erro", data = listWeather)
        }
    }

    fun getAllFavoriteWeather(): MutableList<CityDatabase> = weatherCityDao.getAll()
    fun removeFavoriteWeather(weatherCity: CityDatabase): Int = weatherCityDao.delete(weatherCity)
    fun addFavoriteWeather(weatherCity: CityDatabase): Unit = weatherCityDao.insert(weatherCity)

    suspend fun searchCityById(cityID: Int): Resource<CityDatabase> {
        val retorno = service.searchCityById(cityID, temperatureUnitPreference, languagePreference)
        return if (retorno.status == Status.SUCCESS) {
            val cityDatabase = mapWeatherCityToCityDatabase(retorno.data!!)
            Resource.success(cityDatabase)
        } else {
            Resource.error(msg = retorno.message!!, data = null)
        }
    }

    private fun mapWeatherCityToCityDatabase(data: WeatherCity): CityDatabase {
        val cityDatabase =
            CityDatabaseMapper.map(data, temperatureUnitPreference, languagePreference)
        cityDatabase.favorite = true
        return cityDatabase
    }
}