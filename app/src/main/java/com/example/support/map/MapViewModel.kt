package com.example.support.map

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapViewModel(private val location: LatLng) : ViewModel() {


    fun getCameraUpdate(): CameraUpdate {
        val zoomLevel = 15f
        return CameraUpdateFactory.newLatLngZoom(location, zoomLevel)
    }

    fun getMarkerOption(): MarkerOptions {
        return MarkerOptions().position(location)
    }



}