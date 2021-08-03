package com.example.foursquaremvvm.domain

import com.example.foursquaremvvm.data.remote.model.VenueModel
import com.example.foursquaremvvm.data.repository.VenueRepository
import com.example.foursquaremvvm.util.UseCase
import javax.inject.Inject

class FetchVenuesUseCase @Inject constructor(
    private val repository: VenueRepository
) : UseCase<List<VenueModel?>, FetchVenuesUseCase.Params>() {

    override suspend fun buildUseCase(params: Params) =
        repository.fetchVenues(params.lat, params.lng)

    data class Params(
        val lat: Double,
        val lng: Double
    )
}