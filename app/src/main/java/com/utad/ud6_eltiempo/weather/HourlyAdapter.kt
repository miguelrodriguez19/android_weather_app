package com.utad.ud6_eltiempo.weather

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.utad.ud6_eltiempo.databinding.ItemHourlyTemperaturesBinding
import com.utad.ud6_eltiempo.models.WeatherResponse
import com.utad.ud6_eltiempo.remote.ApiRest
import java.text.SimpleDateFormat
import java.util.*

class HourlyAdapter(var data: List<WeatherResponse.Hourly> = listOf()) :
    RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemHourlyTemperaturesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val hour = binding.tvHour
        private val temperature = binding.tvTemperature
        private val img = binding.ivWeatherIcon

        fun bind(item: WeatherResponse.Hourly) {
            hour.text = formatUnixTime(item.dt.toLong())
            temperature.text = "${item.temp}º"
            val url = "${ApiRest.URL_IMAGES}${item.weather[0].icon}.png"
            Picasso.get().load(url).into(img)
            Log.i("Hourly adapter", url)
        }

        fun formatUnixTime(timestamp: Long): String {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            return sdf.format(Date(timestamp * 1000))
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHourlyTemperaturesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}
