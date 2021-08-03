package com.example.foursquaremvvm.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foursquaremvvm.data.remote.model.LocationModel
import com.example.foursquaremvvm.data.remote.model.VenueModel
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "location")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "address") val address: String? = null,
    @ColumnInfo(name = "lat") val lat: Double = 0.0,
    @ColumnInfo(name = "lng") val lng: Double = 0.0,
    @ColumnInfo(name = "distance") val distance: Int? = null
) {
    fun toLocationModel() = LocationModel(
        id = id,
        address = address,
        lat = lat,
        lng = lng,
        distance = distance,
    )
}