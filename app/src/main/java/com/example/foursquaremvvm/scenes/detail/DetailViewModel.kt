package com.example.foursquaremvvm.scenes.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foursquaremvvm.data.remote.model.VenueModel

class DetailViewModel : ViewModel() {

    val venueModelLiveData = MutableLiveData<VenueModel>()

    fun updateUI(venueModel: VenueModel) {
        venueModelLiveData.value = venueModel
    }
}