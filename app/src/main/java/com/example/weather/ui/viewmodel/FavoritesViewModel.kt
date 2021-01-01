package com.example.weather.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather.data.api.Resource
import com.example.weather.data.api.Status
import com.example.weather.data.databse.dao.WeatherCityDao
import com.example.weather.data.model.CityDatabase
import com.example.weather.data.model.WeatherCity
import com.example.weather.data.model.mapper.CityDatabaseMapper
import com.example.weather.data.repository.WeatherRepository
import com.example.weather.util.UtilSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    val favoritesWeatherLiveData = MutableLiveData<Resource<Any>>()

    fun deleteFavoriteWeather(weatherCity: CityDatabase): Int =
        repository.removeFavoriteWeather(weatherCity)

    fun getFavoritesWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var citiesDatabase: MutableList<CityDatabase> = getFavoritesWeatherFromDatabase()
                var citiesDatabaseActualized = getWeatherActualizedFromApi(citiesDatabase)
                withContext(Dispatchers.Main) {
                    favoritesWeatherLiveData.value = Resource.success(citiesDatabaseActualized)
                }
            } catch (e: Throwable) {
                withContext(Dispatchers.Main) {
                    favoritesWeatherLiveData.value = Resource.error(e.message ?: "", e)
                }
            }
        }
    }

    private suspend fun getFavoritesWeatherFromDatabase(): MutableList<CityDatabase> {
        return repository.getAllFavoriteWeather()
    }

    private suspend fun getWeatherActualizedFromApi(
        list: MutableList<CityDatabase>
    ): MutableList<CityDatabase> {
        var citiesWeather: MutableList<CityDatabase> = arrayListOf()
        list.forEach {
            repository.searchCityById(it.id).apply {
                when (this.status) {
                    Status.SUCCESS -> {
                        citiesWeather.add(this.data!!)
                    }
                    Status.ERROR -> throw Exception(this.message)
                }
            }
        }
        return citiesWeather
    }

    private fun mapWeatherCityToCityDatabase(data: WeatherCity): CityDatabase {
        val cityDatabase = CityDatabaseMapper.map(data, "C", "PT")
        cityDatabase.favorite = true
        return cityDatabase
    }

    class FavoritesViewModelFactory(
        private val weatherCityDao: WeatherCityDao,
        private val utilSharedPreferences: UtilSharedPreferences
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
                return FavoritesViewModel(
                    WeatherRepository(
                        weatherCityDao = weatherCityDao,
                        utilSharedPreferences = utilSharedPreferences
                    )
                ) as T
            }
            throw IllegalArgumentException("Unknown class name")
        }
    }
}