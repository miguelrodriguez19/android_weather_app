package com.utad.ud6_eltiempo.remote

import com.utad.ud6_eltiempo.models.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("3.0/onecall?")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String = ApiRest.appid,
        @Query("lang") lang: String = ApiRest.language,
        @Query("units") units: String = ApiRest.units,
        @Query("exclude") exclude: String = ApiRest.exclude
    ): Response<WeatherResponse>

}