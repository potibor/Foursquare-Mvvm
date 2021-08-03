package com.example.foursquaremvvm.data.remote.api

import com.example.foursquaremvvm.data.remote.model.FoursquareModel
import retrofit2.http.GET
import retrofit2.http.Query

interface VenueService {

    @GET(VENUES)
    suspend fun fetchVenues(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("v") date: String,
        @Query("ll") location: String
    ): FoursquareModel

    companion object {
        const val VENUES = "venues/search"
    }
}