package com.example.foursquaremvvm.scenes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.foursquaremvvm.R
import com.example.foursquaremvvm.data.entity.LocationEntity
import com.example.foursquaremvvm.data.entity.VenueEntity
import com.example.foursquaremvvm.databinding.FragmentHomeBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), OnMapReadyCallback {

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var binding: FragmentHomeBinding

    private lateinit var googleMap: GoogleMap
    private var mapFragment: SupportMapFragment? = null

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
    }

    override fun onMapReady(onReadyMap: GoogleMap) {
        googleMap = onReadyMap
        googleMap.setInfoWindowAdapter(MarkerInfoWindowAdapter(requireContext()))
        val venueEntity =
            VenueEntity(name = "example", location = LocationEntity(address = "example"))
        val marker = googleMap.addMarker(
            MarkerOptions()
                .title(venueEntity.name)
                .position(venueEntity.location?.latLng)
        )
        marker?.tag = venueEntity
    }

}