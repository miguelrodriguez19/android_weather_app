package com.utad.ud6_eltiempo.weather

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.utad.ud6_eltiempo.databinding.ItemDailyTemperaturesBinding
import com.utad.ud6_eltiempo.databinding.ItemHourlyTemperaturesBinding
import com.utad.ud6_eltiempo.models.WeatherResponse
import com.utad.ud6_eltiempo.remote.ApiRest
import java.text.SimpleDateFormat
import java.util.*

class DailyAdapter(var data: List<WeatherResponse.Daily> = listOf()) :
    RecyclerView.Adapter<DailyAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemDailyTemperaturesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val day = binding.tvDay
        private val hum = binding.tvHumidity
        private val max = binding.tvMaxTemp
        private val min = binding.tvMinTemp
        private val img = binding.ivWeather

        fun bind(item: WeatherResponse.Daily) {
            day.text = formatUnixDateTime(item.dt.toLong())
            max.text = "${item.temp.max}º"
            min.text = "${item.temp.min}º"
            hum.text = "${item.humidity}%"
            val url = "${ApiRest.URL_IMAGES}${item.weather[0].icon}.png"
            Picasso.get().load(url).into(img)
            Log.i("Hourly adapter", url)
        }

        private fun formatUnixDateTime(timestamp: Long): String {
            val sdf = SimpleDateFormat("MMMM dd", Locale.getDefault())
            return sdf.format(Date(timestamp * 1000))
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDailyTemperaturesBinding.inflate(
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
