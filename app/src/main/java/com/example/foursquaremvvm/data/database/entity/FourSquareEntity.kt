package com.example.foursquaremvvm.data.database.entity

import androidx.room.*

@Entity(tableName = "foursquare")
data class FourSquareEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int = 0,
    @Ignore val response: ResponseEntity? = null
)