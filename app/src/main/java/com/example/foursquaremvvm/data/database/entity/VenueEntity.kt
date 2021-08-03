package com.example.foursquaremvvm.data.database.entity

import androidx.room.*
import com.example.foursquaremvvm.data.remote.model.VenueModel

@Entity(tableName = "venue")
data class VenueEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "name") var name: String? = "",
    @Ignore var location: LocationEntity? = null
) {
    fun toVenueModel() = VenueModel(
        name = name, location = location?.toLocationModel()
    )
}