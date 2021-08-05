package com.example.foursquaremvvm.scenes.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foursquaremvvm.data.remote.model.VenueModel
import com.example.foursquaremvvm.domain.FetchVenuesUseCase
import com.example.foursquaremvvm.util.Failure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchVenuesUseCase: FetchVenuesUseCase
) : ViewModel() {

    private val _venuesListLiveData = MutableLiveData<List<VenueModel?>>()
    val venuesListLiveData: LiveData<List<VenueModel?>> get() = _venuesListLiveData

    fun fetchVenuesWithLatLng(latitude: Double, longitude: Double) = viewModelScope.launch {
        fetchVenuesUseCase.run(
            FetchVenuesUseCase.Params(
                lat = latitude,
                lng = longitude
            )
        ).either(::handleFailure, ::handleSuccessLiveData)
    }

    private fun handleFailure(failure: Failure) {
        Log.d("TAG: Error --- ", failure.message.toString())
    }

    private fun handleSuccessLiveData(listVenues: List<VenueModel?>) {
        _venuesListLiveData.value = listVenues
    }
}