package com.example.foursquaremvvm.data.entity

import androidx.room.*

@Entity(tableName = "response")
data class ResponseEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int = 0,
    @Ignore val response: List<VenueEntity>? = null
)