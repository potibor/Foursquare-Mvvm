package com.example.foursquaremvvm.data.remote.datasource

import com.example.foursquaremvvm.base.BaseRemoteDataSource
import com.example.foursquaremvvm.data.remote.api.VenueService
import com.example.foursquaremvvm.data.remote.model.FoursquareModel
import com.example.foursquaremvvm.util.Constants
import com.example.foursquaremvvm.util.now
import java.util.*
import javax.inject.Inject

class VenueRemoteDataSource @Inject constructor(
    private val service: VenueService
) : BaseRemoteDataSource() {

    suspend fun fetchVenues(lat: String, lng: String): FoursquareModel = invoke {
        service.fetchVenues(
            Constants.CLIENT_ID,
            Constants.CLIENT_SECRET,
            date = Date().now(),
            location = "$lat, $lng"
        )
    }
}