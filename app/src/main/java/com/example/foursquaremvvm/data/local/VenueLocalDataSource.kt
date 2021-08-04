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

    suspend fun add(locationModel: LocationModel) {
        with(locationModel) {
            venuesDao.addLocation(
                LocationEntity(
                    id = id,
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

    suspend fun getLocation(): LocationModel?  {
        val locationEntity = venuesDao.getLocation()
        return locationEntity?.toLocationModel()
    }


    suspend fun addVenueList(venuesFromService: List<VenueModel?>) {
        val newVenueEntityList: MutableList<VenueEntity?> = mutableListOf()
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
        venuesDao.addVenues(newVenueEntityList)
    }

    suspend fun removeAllVenuesFirst() {
        venuesDao.removeAllVenues()
    }

    suspend fun removeLocationsFromLocal() {
        venuesDao.removeLocations()
    }
}