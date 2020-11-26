package com.example.weather.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.weather.R
import com.example.weather.database.WeatherCityDatabase
import com.example.weather.database.dao.WeatherCityDao
import com.example.weather.manager.InternetConnectionManager
import com.example.weather.model.mapper.CityDatabaseMapper
import com.example.weather.model.CityDatabase
import com.example.weather.model.WeatherBundle
import com.example.weather.retrofit.RetrofitManager
import com.example.weather.retrofit.service.WeatherService
import com.example.weather.ui.adapter.WeatherAdapter
import com.example.weather.util.UtilSharedPreferences
import kotlinx.android.synthetic.main.fragment_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    var listaWeather = mutableListOf<CityDatabase>()
    private lateinit var weatherCityDao: WeatherCityDao;
    private val recyclerViewWeather by lazy { fragment_search_weather_data_list }
    private val buttonSearch by lazy { search_button_fragment_search }
    private val textSearchCity by lazy { search_fragment_search_city }
    private val weatherService: WeatherService = RetrofitManager().noticiaService
    lateinit var utilSharedPreferences: UtilSharedPreferences
    private lateinit var temperatureUnitPreference: String
    private lateinit var languagePreference: String
    private val weatherDataAdapter: WeatherAdapter by lazy {
        WeatherAdapter(
            listaWeather,
            context,
            this::onWeatherClickListener,
            this
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        weatherCityDao = WeatherCityDatabase.getInsanceDatabase(context).weatherCityDao()
        utilSharedPreferences = UtilSharedPreferences(context, Constants.preferenceFilename)
        temperatureUnitPreference = utilSharedPreferences.loadStringValue(Constants.preferenceTemperatureUnit).toString()
        languagePreference = utilSharedPreferences.loadStringValue(Constants.preferenceLanguage).toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? = inflater.inflate(R.layout.fragment_search, container, false)

    //http://api.openweathermap.org/data/2.5/weather?q=Recife,br&APPID=23af5614bbb8fa0d86da0d819c3ebe7b
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWeatherAdapter()
        setupEditTextSearchCity()
        buttonSearch.isEnabled = false

        buttonSearch.setOnClickListener {
            when (view.context.let { InternetConnectionManager.isConnectivityAvailable(it) }) {
                true -> {
                    progressBar.visibility = View.VISIBLE

                    val call =
                        weatherService.findWeatherByCity(
                            search_fragment_search_city.text.toString(),
                            getTemperatureUnit(),
                            getLanguage(),
                            "23af5614bbb8fa0d86da0d819c3ebe7b"
                        )

                    call.enqueue(object : Callback<WeatherBundle> {
                        override fun onResponse(
                            call: Call<WeatherBundle>,
                            response: Response<WeatherBundle>
                        ) {
                            when (response.isSuccessful) {
                                true -> {
                                    progressBar.visibility = View.GONE
                                    response.body()?.run {
                                        val list = CityDatabaseMapper.mapToList(this, temperatureUnitPreference, languagePreference)
                                        weatherDataAdapter.addAll(list)
                                    }
                                    buttonSearch.isEnabled = false
                                    textSearchCity.text.clear()
                                }
                                else -> {
                                    progressBar.visibility = View.GONE
                                    Toast.makeText(
                                        view.context,
                                        "Código de erro : " + response.errorBody()?.string(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<WeatherBundle>, t: Throwable) {
                            progressBar.visibility = View.GONE
                            Toast.makeText(
                                view.context,
                                "Erro: $t",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("IPL", "Erro: $t")
                        }
                    })

                }
                false -> {
                    Toast.makeText(view.context, "Sem conexão com a internet", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun onWeatherClickListener(weatherCity: CityDatabase) {
        weatherCityDao.insert(weatherCity)
        weatherDataAdapter.remove(weatherCity)
        Toast.makeText(
            view?.context,
            "Weather : " + weatherCity.city,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setupWeatherAdapter() {
        recyclerViewWeather.adapter = weatherDataAdapter
        val layoutManager = GridLayoutManager(context, GridLayoutManager.VERTICAL)
        recyclerViewWeather.layoutManager = layoutManager
        recyclerViewWeather.addItemDecoration(WeatherAdapter.WeatherItemDecoration(20))
    }

    private fun setupEditTextSearchCity(){
        textSearchCity.addTextChangedListener {
            buttonSearch.isEnabled = it?.length!! > 2
        }
    }

    private fun getTemperatureUnit(): String {
        return when (temperatureUnitPreference) {
            getString(R.string.fahrenheit_unit) -> "imperial"
            else -> "metric"
        }
    }

    private fun getLanguage(): String {
        return when (languagePreference) {
            getString(R.string.language_english) -> "EN"
            else -> "PT"
        }
    }

}