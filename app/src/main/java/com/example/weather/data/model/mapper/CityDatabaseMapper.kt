package com.example.weather.data.model.mapper

import com.example.weather.data.model.CityDatabase
import com.example.weather.data.model.WeatherBundle
import com.example.weather.data.model.WeatherCity

class CityDatabaseMapper {

    companion object {

        fun map(weatherCity: WeatherCity, temperatureUnitPreference: String?, languagePreference: String): CityDatabase{
            return CityDatabase(
                id = weatherCity.id,
                city = weatherCity.name,
                temperature = weatherCity.main.temp,
                temperature_min = weatherCity.main.tempMin,
                temperature_max = weatherCity.main.tempMax,
                description = weatherCity.weather[0].description,
                favorite = false,
                icon = weatherCity.weather[0].icon,
                temperatureUnit = temperatureUnitPreference,
                language = languagePreference)
        }

        fun mapToList(weatherBundle: WeatherBundle, temperatureUnitPreference: String, languagePreference: String): MutableList<CityDatabase> {
            var listWeatherCity: ArrayList<WeatherCity> = weatherBundle.weatherCity
            val list = mutableListOf<CityDatabase>()

            listWeatherCity.forEach { weatherCity ->
                var cityDatabase = CityDatabase(
                    id = weatherCity.id,
                    city = weatherCity.name,
                    temperature = weatherCity.main.temp,
                    temperature_min = weatherCity.main.tempMin,
                    temperature_max = weatherCity.main.tempMax,
                    description = weatherCity.weather[0].description,
                    favorite = false,
                    icon = weatherCity.weather[0].icon,
                    temperatureUnit = temperatureUnitPreference,
                    language = languagePreference
                )
                list.add(cityDatabase)
            }
            return list
        }
    }
}