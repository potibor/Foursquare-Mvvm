package com.example.foursquaremvvm.data.remote.model

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable


data class LocationModel(
    val id: Int = 0,
    val address: String? = null,
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val distance: Int? = null
): Serializable {
    val latLng: LatLng
        get() {
            return LatLng(lat, lng)
        }
}