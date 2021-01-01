package com.example.weather.ui

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.weather.data.api.Status
import com.example.weather.data.databse.WeatherCityDatabase
import com.example.weather.data.databse.dao.WeatherCityDao
import com.example.weather.data.model.CityDatabase
import com.example.weather.databinding.FragmentFavoritesBinding
import com.example.weather.manager.InternetConnectionManager
import com.example.weather.ui.adapter.WeatherAdapter
import com.example.weather.ui.viewmodel.FavoritesViewModel
import com.example.weather.util.UtilSharedPreferences
import kotlinx.android.synthetic.main.fragment_favorites.*


class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var favoriteViewModel: FavoritesViewModel

    private val weatherDataAdapter: WeatherAdapter by lazy {
        WeatherAdapter(
            context = context,
            onClick = this::onWeatherItemListClickListener,
            fragment = this
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setupViewModel(
            WeatherCityDatabase.getInsanceDatabase(context).weatherCityDao(),
            UtilSharedPreferences(context, Constants.preferenceFilename)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWeatherListAdapter()
        binding.progressBarFavorites.visibility = View.VISIBLE
        getFavoritesWeather()
        setupFavoritesWeatherLiveData()
    }

    private fun onWeatherItemListClickListener(weatherCity: CityDatabase) {
        favoriteViewModel.deleteFavoriteWeather(weatherCity).apply {
            weatherDataAdapter.remove(weatherCity)
        }
    }

    private fun setupWeatherListAdapter() {
        weather_list
        val layoutManager = GridLayoutManager(context, GridLayoutManager.VERTICAL)
        binding.weatherList.adapter = weatherDataAdapter
        binding.weatherList.layoutManager = layoutManager
        binding.weatherList.addItemDecoration(WeatherAdapter.WeatherItemDecoration(20))
    }

    private fun setupViewModel(
        weatherCityDao: WeatherCityDao,
        utilSharedPreferences: UtilSharedPreferences
    ) {
        favoriteViewModel = ViewModelProvider(
            this,
            FavoritesViewModel.FavoritesViewModelFactory(weatherCityDao, utilSharedPreferences)
        ).get(FavoritesViewModel::class.java)
    }

    private fun getFavoritesWeather() {
        try {
            if (InternetConnectionManager.isConnectivityAvailable(requireContext())) favoriteViewModel.getFavoritesWeather()
            else {
                errorAlert(context, "Sem acesso a internet, tente mais tarde.")
                hideProgressBar()
            }
        } catch (e: Throwable) {
            errorAlert(context, e.message)
            hideProgressBar()
        }
    }

    private fun setupFavoritesWeatherLiveData() {
        favoriteViewModel.favoritesWeatherLiveData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> weatherDataAdapter.addAll(it.data as MutableList<CityDatabase>)
                Status.ERROR -> errorAlert(context, it.message)
            }
            hideProgressBar()
        })
    }

    private fun hideProgressBar() {
        binding.progressBarFavorites.visibility = View.GONE
    }

    private fun errorAlert(context: Context?, message: String?) {
        val builder = AlertDialog.Builder(context)
        with(builder)
        {
            setTitle("Erro")
            setMessage(message)
            setPositiveButton(
                "OK",
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                })
            show()
        }
    }
}