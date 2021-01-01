package com.example.weather.data.model

import retrofit2.Response

data class ApiResponse(val response: Response<WeatherBundle>? = null, val error: Throwable? = null)
