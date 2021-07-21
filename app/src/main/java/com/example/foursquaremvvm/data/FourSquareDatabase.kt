package com.example.foursquaremvvm.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.foursquaremvvm.data.entity.FourSquareEntity
import com.example.foursquaremvvm.data.entity.LocationEntity
import com.example.foursquaremvvm.data.entity.ResponseEntity
import com.example.foursquaremvvm.data.entity.VenueEntity

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
abstract class FourSquareDatabase : RoomDatabase()