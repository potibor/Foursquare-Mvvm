package com.example.foursquaremvvm.data.local

import com.example.foursquaremvvm.data.database.dao.FoursquareDao
import com.example.foursquaremvvm.data.database.entity.LocationEntity
import com.example.foursquaremvvm.data.database.entity.VenueEntity
import com.example.foursquaremvvm.data.remote.model.LocationModel
import com.example.foursquaremvvm.data.remote.model.VenueModel
import javax.inject.Inject

class VenueLocalDataSource @Inject constructor(
    private val venuesDao: FoursquareDao
) {

    fun add(locationModel: LocationModel) {
        with(locationModel) {
            venuesDao.addLocation(
                LocationEntity(
                    id = 1,
                    lat = lat,
                    lng = lng
                )
            )
        }

    }

    suspend fun getAll(): List<VenueModel> =
        venuesDao.getVenues().map { venue ->
            VenueEntity(
                id = venue.id,
                name = venue.name,
                location = venue.location
            ).toVenueModel()
        }

    fun getLocation(): LocationModel?  {
        val locationEntity = venuesDao.getLocation()
        return locationEntity?.toLocationModel()
    }


    fun addVenueList(venuesFromService: List<VenueEntity?>) {
        venuesDao.addVenues(venuesFromService)
    }

    fun removeAllVenuesFirst() {
        venuesDao.removeAllVenues()
    }

    fun removeLocationsFromLocal() {
        venuesDao.removeLocations()
    }
}