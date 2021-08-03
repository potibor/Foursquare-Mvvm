package com.example.foursquaremvvm.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.foursquaremvvm.data.database.dao.FoursquareDao
import com.example.foursquaremvvm.data.database.entity.FourSquareEntity
import com.example.foursquaremvvm.data.database.entity.LocationEntity
import com.example.foursquaremvvm.data.database.entity.ResponseEntity
import com.example.foursquaremvvm.data.database.entity.VenueEntity

@Database(
    entities = [
        FourSquareEntity::class,
        ResponseEntity::class,
        VenueEntity::class,
        LocationEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class FourSquareDatabase : RoomDatabase() {

    abstract fun foursquareDao(): FoursquareDao
}