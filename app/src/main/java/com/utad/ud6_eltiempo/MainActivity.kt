package com.utad.ud6_eltiempo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.utad.ud6_eltiempo.databinding.FragmentHomeBinding
import com.utad.ud6_eltiempo.weather.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, HomeFragment()).commit()
    }
}