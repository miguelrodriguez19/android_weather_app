package com.utad.ud6_eltiempo.weather

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.utad.ud6_eltiempo.databinding.FragmentHomeBinding
import com.utad.ud6_eltiempo.models.WeatherResponse
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val TAG = "Home Fragment"
    private lateinit var rvHourly: RecyclerView
    private var adapter: HourlyAdapter? = null
    private val viewModel: WeatherViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvHourly = binding.rvHourlyTemperatures
        initList()
        listenEvents()
        viewModel.getWeather(40.674727,-3.965036)
    }

    private fun initList() {
        val mLayoutManager = LinearLayoutManager(
            context,
            RecyclerView.HORIZONTAL,
            false
        ) // GridLayoutManager(context, 2)
        rvHourly.layoutManager = mLayoutManager
        adapter = HourlyAdapter()
        rvHourly.adapter = adapter
    }

    private fun listenEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.weatherHourlyList.collect {
                        adapter?.data = it
                        adapter?.notifyDataSetChanged()
                    }
                }
                viewModel.actualWeather?.let { updateData(it) }
            }

        }
    }

    private fun updateData(wr: WeatherResponse) {
        wr?.let {
            val data = wr.current
            binding.tvActualDay.text = formatUnixDateTime(data.dt.toLong())
            binding.tvHumidity.text = "${data.humidity}%"
            binding.tvPressure.text = "${data.pressure}hPa"
            binding.tvTemperature.text = "${data.temp}º"
            binding.tvUv.text = "${data.uvi}"
            binding.tvThermalSensation.text = "${data.feelsLike}"
            binding.tvRainProbability.text = "${wr.hourly[0].pop * 100}"
            binding.tvSunset.text = formatUnixTime(data.sunset.toLong())
            binding.tvSunrise.text = formatUnixTime(data.sunrise.toLong())
            binding.tvWindSpeed.text = "${data.windSpeed}km/h"
            binding.ivWindDeg.rotation = data.windDeg.toFloat() - 90
        }
    }

    private fun formatUnixTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp * 1000))
    }

    private fun formatUnixDateTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("MMMM dd, HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp * 1000))
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}