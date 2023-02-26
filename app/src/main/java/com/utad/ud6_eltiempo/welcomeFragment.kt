package com.utad.ud6_eltiempo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.utad.ud6_eltiempo.databinding.FragmentWelcomeBinding
import com.utad.ud6_eltiempo.weather.HomeFragment

class welcomeFragment : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!
    private val PERMISSIONS_REQUEST_LOCATION = 100
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnStart = binding.btnStart
        val tvError = binding.tvError

        btnStart.setOnClickListener {
            // Comprobar si se han concedido los permisos de ubicación
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Si los permisos no se han concedido, solicitarlos
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    PERMISSIONS_REQUEST_LOCATION
                )
            } else {
                // Si se han concedido los permisos, continuar con la lógica de la aplicación
                tvError.text = null
                // ...
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Los permisos se han concedido, continuar con la lógica de la aplicación
                    binding.tvError.text = null
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.mainContainer, HomeFragment()).commit()
                } else {
                    // Los permisos no se han concedido, mostrar un mensaje de error al usuario
                    binding.tvError.text = getString(R.string.accept_permissions)
                }
                return
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}