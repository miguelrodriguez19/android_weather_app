package com.utad.ud6_eltiempo.models


import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val alerts: List<Alert>,
    val current: Current,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Int
) : java.io.Serializable{
    data class Alert(
        val description: String,
        val end: Int,
        val event: String,
        @SerializedName("sender_name")
        val senderName: String,
        val start: Int,
        val tags: List<String>
    ): java.io.Serializable

    data class Current(
        val clouds: Int,
        @SerializedName("dew_point")
        val dewPoint: Double,
        val dt: Int,
        @SerializedName("feels_like")
        val feelsLike: Double,
        val humidity: Int,
        val pressure: Int,
        val sunrise: Int,
        val sunset: Int,
        val temp: Double,
        val uvi: Double,
        val visibility: Int,
        val weather: List<Weather>,
        @SerializedName("wind_deg")
        val windDeg: Int,
        @SerializedName("wind_gust")
        val windGust: Double,
        @SerializedName("wind_speed")
        val windSpeed: Double
    ): java.io.Serializable {
        data class Weather(
            val description: String,
            val icon: String,
            val id: Int,
            val main: String
        ): java.io.Serializable
    }

    data class Daily(
        val clouds: Int,
        @SerializedName("dew_point")
        val dewPoint: Double,
        val dt: Int,
        @SerializedName("feels_like")
        val feelsLike: FeelsLike,
        val humidity: Int,
        @SerializedName("moon_phase")
        val moonPhase: Double,
        val moonrise: Int,
        val moonset: Int,
        val pop: Double,
        val pressure: Int,
        val snow: Double,
        val sunrise: Int,
        val sunset: Int,
        val temp: Temp,
        val uvi: Double,
        val weather: List<Weather>,
        @SerializedName("wind_deg")
        val windDeg: Int,
        @SerializedName("wind_gust")
        val windGust: Double,
        @SerializedName("wind_speed")
        val windSpeed: Double
    ) : java.io.Serializable{
        data class FeelsLike(
            val day: Double,
            val eve: Double,
            val morn: Double,
            val night: Double
        ): java.io.Serializable

        data class Temp(
            val day: Double,
            val eve: Double,
            val max: Double,
            val min: Double,
            val morn: Double,
            val night: Double
        ): java.io.Serializable

        data class Weather(
            val description: String,
            val icon: String,
            val id: Int,
            val main: String
        ): java.io.Serializable
    }

    data class Hourly(
        val clouds: Int,
        @SerializedName("dew_point")
        val dewPoint: Double,
        val dt: Int,
        @SerializedName("feels_like")
        val feelsLike: Double,
        val humidity: Int,
        val pop: Double,
        val pressure: Int,
        val snow: Snow,
        val temp: Double,
        val uvi: Double,
        val visibility: Int,
        val weather: List<Weather>,
        @SerializedName("wind_deg")
        val windDeg: Int,
        @SerializedName("wind_gust")
        val windGust: Double,
        @SerializedName("wind_speed")
        val windSpeed: Double
    ): java.io.Serializable {
        data class Snow(
            @SerializedName("1h")
            val h: Double
        ): java.io.Serializable

        data class Weather(
            val description: String,
            val icon: String,
            val id: Int,
            val main: String
        ): java.io.Serializable
    }
}