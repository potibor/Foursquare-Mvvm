package com.example.foursquaremvvm.scenes.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.example.foursquaremvvm.R
import com.example.foursquaremvvm.data.database.entity.VenueEntity
import com.example.foursquaremvvm.data.remote.model.VenueModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class MarkerInfoWindowAdapter(
    private val context: Context
) : GoogleMap.InfoWindowAdapter {

    override fun getInfoContents(marker: Marker?): View? {
        val venue = marker?.tag as? VenueModel ?: return null
        val view = LayoutInflater.from(context).inflate(R.layout.item_marker, null)

        view.findViewById<AppCompatTextView>(R.id.text_view_marker_name).text = venue.name
        view.findViewById<AppCompatTextView>(R.id.text_view_marker_address).text =
            venue.location?.address
        return view
    }

    override fun getInfoWindow(marker: Marker?): View? {
        // Return null to indicate that the
        // default window (white bubble) should be used
        return null
    }
}