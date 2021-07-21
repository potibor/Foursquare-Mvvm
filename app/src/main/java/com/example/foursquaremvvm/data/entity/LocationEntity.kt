package com.example.foursquaremvvm.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
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
    val latLng: LatLng
        get() {
            return LatLng(lat, lng)
        }
}