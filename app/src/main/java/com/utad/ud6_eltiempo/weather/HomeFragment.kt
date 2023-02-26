package com.utad.ud6_eltiempo.weather

import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.squareup.picasso.Picasso
import com.utad.ud6_eltiempo.databinding.FragmentHomeBinding
import com.utad.ud6_eltiempo.models.WeatherResponse
import com.utad.ud6_eltiempo.remote.ApiRest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val TAG = "Home Fragment"
    private var latitude: Double? = null
    private var longitude: Double? = null
    private lateinit var pbLoading: View
    private lateinit var swiperefresh: SwipeRefreshLayout
    private lateinit var rvHourly: RecyclerView
    private lateinit var rvDaily: RecyclerView
    private var hourlyAdapter: HourlyAdapter? = null
    private var dailyAdapter: DailyAdapter? = null
    private val viewModel: WeatherViewModel by activityViewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvHourly = binding.rvHourlyTemperatures
        rvDaily = binding.rvDailyTemperatures
        pbLoading = binding.pbLoading
        initList()
        listenEvents()
        fusedLocationClient.lastLocation
            .addOnSuccessListener { lct: Location? ->
                if (lct != null) {
                    latitude = lct.latitude
                    longitude = lct.longitude
                    viewModel.getWeather(latitude!!, longitude!!)
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    val addresses =
                        geocoder.getFromLocation(lct.latitude, lct.longitude, 1)
                    if (addresses != null) {
                        if (addresses.isNotEmpty()) {
                            val cityName: String = addresses[0].locality
                            binding.tvCity.text = cityName
                        }
                    }
                }
            }
    }

    private fun initList() {
        rvHourly.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.HORIZONTAL,
            false
        )
        rvDaily.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )
        hourlyAdapter = HourlyAdapter()
        rvHourly.adapter = hourlyAdapter
        dailyAdapter = DailyAdapter()
        rvDaily.adapter = dailyAdapter
    }

    private fun listenEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.weatherHourlyList.collect {
                        hourlyAdapter?.data = it
                        hourlyAdapter?.notifyDataSetChanged()
                    }
                }
                launch {
                    viewModel.weatherDailyList.collect {
                        viewModel.weatherCurrent?.let { it1 -> updateData(it1) }
                        //binding.tvRainProbability.text =                            "${(viewModel.weatherHourlyList as List<WeatherResponse.Hourly>)[0].pop * 100}"
                        dailyAdapter?.data = it
                        dailyAdapter?.notifyDataSetChanged()
                    }
                }
                launch {
                    viewModel.loading.collect {
                        pbLoading.isVisible = it
                    }
                }
            }

        }
    }

    private fun updateData(data: WeatherResponse.Current) {
        data.let {
            binding.tvActualDay.text = formatUnixDateTime(data.dt.toLong()).replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
            }
            binding.tvHumidity.text = "${data.humidity}%"
            binding.tvPressure.text = "${data.pressure}hPa"
            binding.tvTemperature.text = "${(data.temp * 10).toInt() / 10.0}º"
            binding.tvUv.text = "${data.uvi}"
            binding.tvThermalSensation.text = "${data.feelsLike}"
            binding.tvSunset.text = formatUnixTime(data.sunset.toLong())
            binding.tvSunrise.text = formatUnixTime(data.sunrise.toLong())
            binding.tvWindSpeed.text = "${data.windSpeed}km/h"
            binding.ivWindDeg.rotation = data.windDeg.toFloat() + 90

            val url = "${ApiRest.URL_IMAGES}${data.weather[0].icon}@2x.png"
            Picasso.get().load(url).into(binding.ivTemperatureIcon)
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