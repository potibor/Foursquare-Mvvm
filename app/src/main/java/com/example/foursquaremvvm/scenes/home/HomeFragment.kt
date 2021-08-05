package com.example.foursquaremvvm.scenes.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.foursquaremvvm.NavGraphDirections
import com.example.foursquaremvvm.R
import com.example.foursquaremvvm.data.remote.model.VenueModel
import com.example.foursquaremvvm.databinding.FragmentHomeBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private val viewModel by viewModels<HomeViewModel>()

    private var mapFragment: SupportMapFragment? = null
    private lateinit var binding: FragmentHomeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var googleMap: GoogleMap

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getLastKnownLocation()
        } else {
            requestLocationIfNeeded()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        observeVenuesList()
    }

    private fun requestLocationIfNeeded() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {
                getLastKnownLocation()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    moveCameraToTheCurrentLocation(location)
                    viewModel.fetchVenuesWithLatLng(location.latitude, location.longitude)
                }
            }
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(onReadyMap: GoogleMap) {
        googleMap = onReadyMap
        googleMap.setInfoWindowAdapter(MarkerInfoWindowAdapter(requireContext()))
        googleMap.setOnMapClickListener { location ->
            viewModel.fetchVenuesWithLatLng(location.latitude, location.longitude)
        }
        googleMap.setOnMarkerClickListener(this)
        requestLocationIfNeeded()
    }

    private fun observeVenuesList() {
        viewModel.venuesListLiveData.observe(viewLifecycleOwner) {
            googleMap.clear()
            it.forEach { venueModel ->
                addMarkerToMap(venueModel)
            }
        }
    }

    private fun addMarkerToMap(venueModel: VenueModel?) {
        venueModel?.location?.let {
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .title(venueModel.name)
                    .position(venueModel.location.latLng)
            )
            marker?.tag = venueModel
        }
    }

    private fun moveCameraToTheCurrentLocation(location: Location?) {
        val zoomLevel = 16.0f

        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    location?.latitude ?: 0.0,
                    location?.longitude ?: 0.0
                ), zoomLevel
            )
        )
    }

    override fun onMarkerClick(marker: Marker): Boolean  {
        val venue = marker.tag as? VenueModel ?: return false
        findNavController().navigate(NavGraphDirections.actionHomeFragmentToDetailFragment(venue))
        return false
    }

}