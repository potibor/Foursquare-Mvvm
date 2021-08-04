package com.example.foursquaremvvm.data.remote.model

import java.io.Serializable

data class VenueModel(
    val name: String?,
    val location: LocationModel?
): Serializable