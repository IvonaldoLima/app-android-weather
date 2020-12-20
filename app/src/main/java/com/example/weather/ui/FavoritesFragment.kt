package com.example.weather.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.weather.R
import com.example.weather.database.WeatherCityDatabase
import com.example.weather.database.dao.WeatherCityDao
import com.example.weather.model.CityDatabase
import com.example.weather.ui.adapter.WeatherAdapter
import com.example.weather.ui.viewmodel.factory.FavoritesFragmentViewModelFactory
import com.example.weather.ui.viewmodel.FavoritesFragmentViewModel
import kotlinx.android.synthetic.main.fragment_favorites.*


class FavoritesFragment : Fragment() {

    private lateinit var favoriteViewModel: FavoritesFragmentViewModel
    var listaWeather = mutableListOf<CityDatabase>()

    lateinit var weatherCityDao: WeatherCityDao
    private val recyclerViewWeather by lazy { fragment_favorites_weather_data_list }

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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel(weatherCityDao)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_favorites, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWeatherListAdapter()
        loadWeatherList()
    }

    private fun onWeatherClickListener(weatherCity: CityDatabase) {
        favoriteViewModel.removeFavoriteWeather(weatherCity).apply {
            Log.i("IPL", "Linhas deletadas $this")
            weatherDataAdapter.remove(weatherCity)
        }
    }

    private fun setupWeatherListAdapter() {
        recyclerViewWeather.adapter = weatherDataAdapter
        val layoutManager = GridLayoutManager(context, GridLayoutManager.VERTICAL)
        recyclerViewWeather.layoutManager = layoutManager
        recyclerViewWeather.addItemDecoration(WeatherAdapter.WeatherItemDecoration(20))
    }

    private fun loadWeatherList() {
        weatherDataAdapter.addAll(favoriteViewModel.getAllFavoriteWeather())
    }

    private fun setupViewModel(weatherCityDao: WeatherCityDao) {
        favoriteViewModel = ViewModelProvider(
            this,
            FavoritesFragmentViewModelFactory(weatherCityDao)
        ).get(FavoritesFragmentViewModel::class.java)
    }
}