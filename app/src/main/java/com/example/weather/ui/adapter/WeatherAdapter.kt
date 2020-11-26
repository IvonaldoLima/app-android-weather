package com.example.weather.ui.adapter

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.model.CityDatabase
import kotlinx.android.synthetic.main.item_weather.view.*


class WeatherAdapter(
    private val setCity: MutableList<CityDatabase>,
    private val context: Context?,
    private val onClick: (CityDatabase) -> Unit,
    private val fragment: Fragment
) :
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class WeatherViewHolder(
        view: View,
        searchFragment: Fragment,
        onClick: (CityDatabase) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val temperature: TextView = view.item_weather_temperature
        private val city: TextView = view.item_weather_city
        private var imgWeather: ImageView = view.item_weather_image
        private val iconFavorite: ImageView = view.item_weather_favorite
        private var fragment = searchFragment
        private var weatherDescription: TextView = view.item_weather_description
        private var temperatureMin: TextView = view.item_weather_min
        private var temperatureMax: TextView = view.item_weather_max
        private var id: TextView = view.item_weather_id
        private var currentWeatherCity: CityDatabase? = null
        private var tempUnitCelsius = view.resources.getString(R.string.celsius_unit)
        private var tempUnitFahrenheit = view.resources.getString(R.string.fahrenheit_unit)

        init {
            iconFavorite.setOnClickListener {
                currentWeatherCity?.run {
                    if (this.favorite) {
                        iconFavorite.setImageResource(R.drawable.ic_action_favorite)
                        this.favorite = false
                    } else {
                        iconFavorite.setImageResource(R.drawable.ic_action_favorite_selected)
                        this.favorite = true
                    }
                    onClick(this)
                }
            }
        }

        fun bind(weatherCity: CityDatabase) {

            val unitTemp = checkTemperatureUnit(weatherCity)
            currentWeatherCity = weatherCity
            city.text = weatherCity.city
            weatherDescription.text = weatherCity.description
            id.text = weatherCity.id.toString()
            temperature.text = "${weatherCity.temperature.toString()} ${unitTemp}"
            temperatureMin.text = "${weatherCity.temperature_min.toString()} ${unitTemp}"
            temperatureMax.text = "${weatherCity.temperature_max.toString()} ${unitTemp}"

            if (weatherCity.favorite) iconFavorite.setImageResource(R.drawable.ic_action_favorite_selected)

            weatherCity.icon?.let {
                Glide.with(fragment)
                    .load("http://openweathermap.org/img/wn/${weatherCity.icon}@2x.png")
                    .centerCrop()
                    .error(R.drawable.ic_broke_image)
                    .into(imgWeather)
            }
        }

        private fun checkTemperatureUnit(weatherCity: CityDatabase): String {
            return if (weatherCity.temperatureUnit == tempUnitCelsius) tempUnitCelsius
            else if (weatherCity.temperatureUnit == tempUnitFahrenheit) tempUnitFahrenheit
            else tempUnitCelsius
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_weather, viewGroup, false)
        return WeatherViewHolder(view, fragment, onClick)
    }

    override fun onBindViewHolder(wetherViewHolder: WeatherViewHolder, position: Int) {
        val flower = setCity[position]
        wetherViewHolder.bind(flower)
    }

    override fun getItemCount() = setCity.size

    fun add(weatherCity: CityDatabase) {
        setCity.clear()
        setCity.add(weatherCity)
        notifyItemChanged(itemCount)
    }

    fun addAll(list: MutableList<CityDatabase>) {
        setCity.clear()
        setCity.addAll(list)
        notifyDataSetChanged()
    }

    fun remove(weatherCity: CityDatabase) {
        val position = setCity.indexOf(weatherCity)
        if (setCity.remove(weatherCity)) notifyItemRemoved(position)
    }

    class WeatherItemDecoration(private val size: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            with(outRect) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    top = size
                }
                left = size
                right = size
                bottom = size
            }
        }
    }
}
