package com.example.foursquaremvvm.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.foursquaremvvm.data.entity.FourSquareEntity

@Database(
    entities = [FourSquareEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FourSquareDatabase : RoomDatabase() {


}