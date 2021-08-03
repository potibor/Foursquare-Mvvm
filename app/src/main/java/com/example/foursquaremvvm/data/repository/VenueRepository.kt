package com.example.foursquaremvvm.data.repository

import android.location.Location
import com.example.foursquaremvvm.data.database.entity.LocationEntity
import com.example.foursquaremvvm.data.database.entity.VenueEntity
import com.example.foursquaremvvm.data.local.VenueLocalDataSource
import com.example.foursquaremvvm.data.remote.datasource.VenueRemoteDataSource
import com.example.foursquaremvvm.data.remote.model.LocationModel
import com.example.foursquaremvvm.data.remote.model.VenueModel
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class VenueRepository @Inject constructor(
    private val remoteDataSource: VenueRemoteDataSource,
    private val localDataSource: VenueLocalDataSource
) {
    suspend fun fetchVenues(lat: Double, lng: Double): List<VenueModel?> {
        val localLastLocation = getLatLngFromLocal()
        val distanceBetweenLocations = calculateDistance(
            lat,
            lng,
            localLastLocation.latitude,
            localLastLocation.longitude
        )
        removeAllLocations()
        addNewLatLngToLocal(lat, lng)

        return if (distanceBetweenLocations > 0.2) {
            val response = remoteDataSource.fetchVenues(lat.toString(), lng.toString())
            val venuesFromService = response.response.venues
            addVenuesToLocal(venuesFromService)
            venuesFromService
        } else {
            removeAllVenues()
            fetchLocalVenues()
        }
    }

    private fun addVenuesToLocal(venuesFromService: List<VenueModel?>) {
        val newVenueEntityList: ArrayList<VenueEntity?> = arrayListOf()
        venuesFromService.forEach {
            newVenueEntityList.add(
                VenueEntity(
                    id = it.hashCode(),
                    name = it?.name,
                    location = LocationEntity(
                        id = it?.location.hashCode(),
                        address = "",
                        lat = it?.location?.lat!!,
                        lng = it.location.lng
                    )
                )
            )
        }
        localDataSource.addVenueList(newVenueEntityList)
    }

    private suspend fun fetchLocalVenues(): List<VenueModel> {
        return localDataSource.getAll()
    }

    private fun removeAllVenues() {
        localDataSource.removeAllVenuesFirst()
    }

    private fun removeAllLocations() {
        localDataSource.removeLocationsFromLocal()
    }

    private fun addNewLatLngToLocal(lat: Double, lng: Double) {
        localDataSource.add(LocationModel(lat = lat, lng = lng))
    }

    private fun getLatLngFromLocal(): LatLng {
        val locationModel = localDataSource.getLocation() ?: return LatLng(0.0, 0.0)
        return LatLng(locationModel.lat, locationModel.lng)
    }

    private fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val lngDiff = lng1 - lng2
        var distance = sin(deg2rad(lat1)) *
                sin(deg2rad(lat2)) +
                cos(deg2rad(lat1)) *
                cos(deg2rad(lat2)) *
                cos(deg2rad(lngDiff))
        distance = rad2deg(acos(distance))

        //Distance in miles
        distance *= 60 * 1.1515

        //Distance in kms
        distance *= 1.609344

        return distance
    }

    // This function converts decimal degrees to radians
    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    //  This function converts radians to decimal degrees
    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
}