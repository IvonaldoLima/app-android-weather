package com.example.weather.util

import java.text.DecimalFormat

class TemperatureConverterUtil {

    companion object {

        private const val constantKelvin: Float = 273.15F
        private val decimalFormat = DecimalFormat("###.00")

        fun kelvinToCelsius(temp: Float): Float {
            val result = temp - constantKelvin
            val resultFormatted = decimalFormat.format(result)
            return resultFormatted.toFloat()
        }
        fun kelvinToFahrenheit(temp: Float): Float {
            val result = (temp - constantKelvin) * 1.8F + 32
            val resultFormatted = decimalFormat.format(result)
            return resultFormatted.toFloat()
        }
    }
}