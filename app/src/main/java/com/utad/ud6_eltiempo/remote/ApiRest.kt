package com.utad.ud6_eltiempo.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiRest {
    lateinit var service: ApiService
    val units = "metric"
    val exclude = "minutely"
    val URL = "https://api.openweathermap.org/data/"
    val URL_IMAGES = "http://openweathermap.org/img/wn/" // añadir al final-> @2x.png
    val appid = "13e878fd690b4045d4e297c798ef8c35"
    val language = "en"

    init {
        initService()
    }

    private fun initService() {
        val retrofit = Retrofit.Builder()
            .baseUrl( URL)
            .addConverterFactory( GsonConverterFactory .create())
            .build()
        service = retrofit.create(ApiService::class.java)
    }
}