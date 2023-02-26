package com.utad.ud6_eltiempo.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utad.ud6_eltiempo.models.WeatherResponse
import com.utad.ud6_eltiempo.remote.ApiRest
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    val weatherHourlyList = MutableStateFlow(listOf<WeatherResponse.Hourly>())
    var weatherCurrent : WeatherResponse.Current? = null
    var weatherDailyList = MutableStateFlow(listOf<WeatherResponse.Daily>())
    val loading = MutableStateFlow(false)
    fun getWeather(lat:Double, lon:Double) {
        loading.value = true
        val coroutineExceptionHandler : CoroutineExceptionHandler =
            (CoroutineExceptionHandler { _, throwable ->
                throwable.printStackTrace()
                loading.value = false
            })
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler , block = {
            val response = ApiRest.service.getWeather(lat, lon)
            if (response.isSuccessful && response.body() != null) {
                weatherHourlyList.value = response.body()!!.hourly
                weatherCurrent = response.body()!!.current
                weatherDailyList.value = response.body()!!.daily
                //actualWeather = response.body()!!
            } else {
                Log.i("WeatherViewModel" , "getGenres: ${response.errorBody()?.string() }")
            }
            loading.value = false
        })
    }
}