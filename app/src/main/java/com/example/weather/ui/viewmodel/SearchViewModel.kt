package com.example.weather.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather.data.api.Resource
import com.example.weather.data.model.CityDatabase
import com.example.weather.data.model.WeatherBundle
import com.example.weather.data.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    val weatherBundleLiveData = MutableLiveData<Resource<MutableList<CityDatabase>>>()

    fun searchWeatherCity(cityName: String) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val resource = repository.searchWeatherCity(cityName)
                withContext(Dispatchers.Main) {
                    weatherBundleLiveData.value = resource
                }

            }
        } catch (e: Throwable) {
            Log.e("IPL", "Error: $e")
        }
    }

    fun addFavoriteWeather(weatherCity: CityDatabase) = repository.addFavoriteWeather(weatherCity)

    class SearchViewModelFactory(
        private val weatherRepository: WeatherRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                return SearchViewModel(weatherRepository) as T
            }
            throw IllegalArgumentException("Unknown class name")
        }
    }

}
