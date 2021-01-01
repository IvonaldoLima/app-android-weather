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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.weather.data.api.Status
import com.example.weather.data.databse.WeatherCityDatabase
import com.example.weather.data.databse.dao.WeatherCityDao
import com.example.weather.data.model.CityDatabase
import com.example.weather.data.repository.WeatherRepository
import com.example.weather.databinding.FragmentSearchBinding
import com.example.weather.ui.adapter.WeatherAdapter
import com.example.weather.ui.viewmodel.SearchViewModel
import com.example.weather.util.UtilSharedPreferences
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchViewModel: SearchViewModel
    private val weatherDataAdapter: WeatherAdapter by lazy {
        WeatherAdapter(
            context,
            this::onWeatherClickListener,
            this
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setupViewModel(WeatherCityDatabase.getInsanceDatabase(context).weatherCityDao()
            , UtilSharedPreferences(context, Constants.preferenceFilename))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWeatherAdapter()
        enableSearchButtonAfterTypeMoreThanTwoChar()
        search_button
        binding.searchButton.isEnabled = false
        setupWeatherBundleLiveData()
        setupOnClickSearchButton()
    }

    private fun setupOnClickSearchButton() {
        binding.searchButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            searchViewModel.searchWeatherCity(
                binding.cityName.text.toString()
            )
        }
    }

    private fun setupWeatherBundleLiveData() {
        searchViewModel.weatherBundleLiveData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    it.message?.let { it1 -> showToast(it1) }
                }
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    if (it.data != null) weatherDataAdapter.addAll(it.data)
                }
                Status.LOADING -> {
                    Log.i("IPL", "Resource Loading: ")
                }
            }
        })
    }

    private fun onWeatherClickListener(weatherCity: CityDatabase) {
        searchViewModel.addFavoriteWeather(weatherCity).apply {
            weatherDataAdapter.remove(weatherCity)
        }
    }

    private fun setupWeatherAdapter() {
        val layoutManager = GridLayoutManager(context, GridLayoutManager.VERTICAL)
        binding.weatherList.adapter = weatherDataAdapter
        binding.weatherList.layoutManager = layoutManager
        binding.weatherList.addItemDecoration(WeatherAdapter.WeatherItemDecoration(20))
    }

    private fun enableSearchButtonAfterTypeMoreThanTwoChar() {
        city_name
        binding.cityName.addTextChangedListener {
            binding.searchButton.isEnabled = it?.length!! > 2
        }
    }

    private fun setupViewModel(weatherCityDao: WeatherCityDao, utilSharedPreferences: UtilSharedPreferences) {
        searchViewModel = ViewModelProvider(
            this,
            SearchViewModel.SearchViewModelFactory(WeatherRepository(weatherCityDao = weatherCityDao, utilSharedPreferences = utilSharedPreferences))
        ).get(SearchViewModel::class.java)
    }

    private fun showToast(
        message: String
    ) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

}