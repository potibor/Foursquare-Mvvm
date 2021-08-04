package com.example.foursquaremvvm.data.remote.model

import java.io.Serializable

data class ResponseModel(
    val venues: List<VenueModel?>
): Serializable