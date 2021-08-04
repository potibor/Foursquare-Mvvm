package com.example.foursquaremvvm.data.repository

import com.example.foursquaremvvm.data.local.VenueLocalDataSource
import com.example.foursquaremvvm.data.remote.datasource.VenueRemoteDataSource
import com.example.foursquaremvvm.data.remote.model.LocationModel
import com.example.foursquaremvvm.data.remote.model.VenueModel
import org.jetbrains.annotations.TestOnly
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
            localLastLocation?.lat ?: 0.0,
            localLastLocation?.lng ?: 0.0
        )
        removeAllLocations()
        addNewLatLngToLocal(lat, lng)

        return if (distanceBetweenLocations > 0.2) {
            val response = remoteDataSource.fetchVenues(lat.toString(), lng.toString())
            val venuesFromService = response.response.venues
            addVenuesToLocal(venuesFromService)
            venuesFromService
        } else {
            fetchLocalVenues()
        }
    }

    @TestOnly
    suspend fun addVenuesToLocal(venuesFromService: List<VenueModel?>) {
        localDataSource.addVenueList(venuesFromService)
    }

    @TestOnly
    suspend fun fetchLocalVenues(): List<VenueModel> {
        val localVenues = localDataSource.getAll()
        removeAllVenues()
        return localVenues
    }

    @TestOnly
    suspend fun removeAllVenues() {
        localDataSource.removeAllVenuesFirst()
    }

    @TestOnly
    suspend fun removeAllLocations() {
        localDataSource.removeLocationsFromLocal()
    }

    @TestOnly
    suspend fun addNewLatLngToLocal(lat: Double, lng: Double) {
        localDataSource.add(LocationModel(lat = lat, lng = lng))
    }

    @TestOnly
    suspend fun getLatLngFromLocal(): LocationModel? {
        return localDataSource.getLocation()
    }

    @TestOnly
    fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
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