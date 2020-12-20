package com.example.weather.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.weather.database.dao.WeatherCityDao
import com.example.weather.model.CityDatabase

class FavoritesFragmentViewModel(
    private val weatherCityDao: WeatherCityDao
) : ViewModel() {

    init {
        Log.i("IPL", "Criando ViewModel" + this.toString())
    }

    fun getAllFavoriteWeather(): MutableList<CityDatabase> = weatherCityDao.getAll()
    fun removeFavoriteWeather(weatherCity: CityDatabase): Int = weatherCityDao.delete(weatherCity)
}