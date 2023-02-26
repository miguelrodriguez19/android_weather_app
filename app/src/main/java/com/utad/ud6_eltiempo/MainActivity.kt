package com.utad.ud6_eltiempo

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.utad.ud6_eltiempo.weather.HomeFragment

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_PERMISSIONS = 1
    private val REQUIRED_PERMISSIONS = arrayOf(
        //Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (permissionsGranted()) {
            // El permiso ya se ha concedido, ir directamente a HomeFragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, HomeFragment())
                .commit()
        } else {
            // El permiso no se ha concedido, ir a WelcomeFragment para solicitar permisos
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, welcomeFragment())
                .commit()
        }


    }
    fun checkPermissions(): Boolean {
        if (!permissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
            return true
        }
        return false
    }

    fun permissionsGranted(): Boolean = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
}