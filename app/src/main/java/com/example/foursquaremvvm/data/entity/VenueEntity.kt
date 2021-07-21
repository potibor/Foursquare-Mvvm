package com.example.foursquaremvvm.data.entity

import androidx.room.*

@Entity(tableName = "venue")
data class VenueEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "name") var name: String? = "",
    @Ignore var location: LocationEntity? = null
)