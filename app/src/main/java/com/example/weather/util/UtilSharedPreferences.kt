package com.example.weather.util

import android.content.Context
import android.content.SharedPreferences

class UtilSharedPreferences(context: Context, sharedPreferencesFileName: String) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE)
    }

    fun editValue(key:String, value: String){
        if (sharedPreferences != null) {
            with(sharedPreferences!!.edit()) {
                putString(key, value)
                apply()
            }
        }
    }

    fun getValue(key: String, defaultValue: String? = ""): String? = sharedPreferences.getString(key, defaultValue)

}

