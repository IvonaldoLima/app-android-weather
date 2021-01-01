package com.example.weather.util

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TemperatureConverterUtilTest(){

    @Test
    fun givenThreeHundredAndTenDegreesKelvin_whenConvertToCelsius_thenReturnThirtySixAndEightyFiveCelsius(){
        var temp = TemperatureConverterUtil.kelvinToCelsius(310F)
        assertEquals(36.85F, temp)
    }

    @Test
    fun givenThreeHundredAndTenDegreesKelvin_whenConvertToFahrenheit_thenReturnNinetyEightAndThirtyThreeFahrenheit(){
        var temp = TemperatureConverterUtil.kelvinToFahrenheit(310F)
        assertEquals(98.33F, temp)
    }
}