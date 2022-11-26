package com.lugares_mario.ui.gallery

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.Manifest
import com.lugares_mario.databinding.FragmentGalleryBinding
import com.lugares_mario.model.Lugar
import com.lugares_mario.viewmodel.GalleryViewModel
import com.lugares_mario.viewmodel.LugarViewModel

class GalleryFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    //será el objeto para interactuar con la vista
    private lateinit var googleMap: GoogleMap
    private var mapReady = false

    //Ojo que se toman los datos de lugarViewModel
    private lateinit var lugarViewModel: LugarViewModel

    //Esta es una función especial... cuando se crea el activity se ejecuta
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //Se solicita al actualización del mapa... despliegue en la pantalla
        binding.map.onCreate(savedInstanceState)
        binding.map.onResume()
        binding.map.getMapAsync(this)
    }

    //Cuando el mapa está listo apra mostrarse...
    override fun onMapReady(map: GoogleMap) {
        map.let {
            googleMap = it
            mapReady = true
            //Se instruye al mapa para que se actualicen las ubicaciones... segun el viewModel
            lugarViewModel.getLugares.observe(viewLifecycleOwner) { lugares ->
                updateMap(lugares)
                ubicaGPS()
            }
        }
    }

    //Para cada ubicación de lugares... se coloca una marca
    private fun updateMap(lugares: List<Lugar>) {
        if (mapReady) {
            lugares.forEach { lugar ->
                if (lugar.latitud?.isFinite() == true && lugar.longitud?.isFinite() == true) {
                    val marker = LatLng(lugar.latitud, lugar.longitud)
                    googleMap.addMarker(MarkerOptions().position(marker).title(lugar.nombre))
                }
            }
        }
    }
    private fun ubicaGPS() {
        val fusedLocation: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION), 105)
        }
        fusedLocation.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    LatLng(9.97, -84.00/*location.latitude,location.longitude*/), 15f)
                googleMap.animateCamera(cameraUpdate)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        lugarViewModel = ViewModelProvider(this)[LugarViewModel::class.java]

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}