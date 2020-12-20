package com.example.weather.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather.database.dao.WeatherCityDao
import com.example.weather.ui.viewmodel.FavoritesFragmentViewModel

class FavoritesFragmentViewModelFactory(private val weatherCityDao: WeatherCityDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesFragmentViewModel::class.java)) {
            return FavoritesFragmentViewModel(weatherCityDao) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}