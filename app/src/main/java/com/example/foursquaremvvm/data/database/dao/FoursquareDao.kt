package com.example.foursquaremvvm.data.database.dao

import androidx.room.*
import com.example.foursquaremvvm.data.database.entity.LocationEntity
import com.example.foursquaremvvm.data.database.entity.VenueEntity

@Dao
interface FoursquareDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLocation(locationEntity: LocationEntity)

    @Query("SELECT * FROM venue")
    suspend fun getVenues(): List<VenueEntity>

    @Query("SELECT * FROM location")
    suspend fun getLocation(): LocationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addVenues(venues: List<VenueEntity?>)

    @Query("DELETE FROM venue")
    suspend fun removeAllVenues()

    @Query("DELETE FROM location")
    suspend fun removeLocations()
}