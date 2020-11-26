package com.example.weather.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weather.R
import com.example.weather.util.UtilSharedPreferences
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment() {

    private val radioButtonEnglish: RadioButton by lazy { fragment_settings_english }
    private val radioButtonPortuguese: RadioButton by lazy { fragment_settings_portuguese }
    private val radioButtonCelsius: RadioButton by lazy { fragment_settings_radio_button_celsius }
    private val radioButtonFahrenheit: RadioButton by lazy { fragment_settings_radio_button_fahrenheit }
    lateinit var utilSharedPreferences: UtilSharedPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)
        utilSharedPreferences = UtilSharedPreferences(context, Constants.preferenceFilename)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSaveButton()
    }

    override fun onStart() {
        super.onStart()
        loadPreferences()
    }

    private fun savePreferenceLanguage(language: String) {
        utilSharedPreferences.editValue(Constants.preferenceLanguage, language)
    }

    private fun savePreferenceTemperatureUnit(temperatureUnit: String) {
        utilSharedPreferences.editValue(Constants.preferenceTemperatureUnit, temperatureUnit)
    }

    private fun setupSaveButton() {
        fragment_settings_save.setOnClickListener {
            when (radio_group_language.checkedRadioButtonId) {
                radioButtonEnglish.id -> savePreferenceLanguage(getString(R.string.language_english))
                radioButtonPortuguese.id -> savePreferenceLanguage(getString(R.string.language_portuguese))
            }
            when (radio_group_temperature_unit.checkedRadioButtonId) {
                radioButtonCelsius.id -> savePreferenceTemperatureUnit(getString(R.string.celsius_unit))
                radioButtonFahrenheit.id -> savePreferenceTemperatureUnit(getString(R.string.fahrenheit_unit))
            }
            Toast.makeText(context, "Settings saved", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadPreferences() {
        val prefTemperature =
            utilSharedPreferences.loadStringValue(Constants.preferenceTemperatureUnit)
        val prefLanguage = utilSharedPreferences.loadStringValue(Constants.preferenceLanguage)
        loadTemperaturePreference(prefTemperature)
        loadLanguagePreference(prefLanguage)
    }

    private fun loadLanguagePreference(languagePreference: String?) {
        if (radioButtonEnglish.text.toString() == languagePreference)
            radioButtonEnglish.isChecked = true
        else radioButtonPortuguese.isChecked = true
    }

    private fun loadTemperaturePreference(temperatureUnitPreference: String?) {
        if (radioButtonFahrenheit.text == temperatureUnitPreference)
            radioButtonFahrenheit.isChecked = true
        else radioButtonCelsius.isChecked = true
    }

}
