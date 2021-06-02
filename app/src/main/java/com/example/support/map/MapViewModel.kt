package com.example.support.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.support.networking.TeamMember
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions

class MapViewModel(member: TeamMember) : ViewModel() {


    fun getCameraUpdate(): CameraUpdate {
        val zoomLevel = 15f
        return CameraUpdateFactory.newLatLngZoom(selectedProperty.value?.location!!, zoomLevel)
    }

    fun getMarkerOption(): MarkerOptions {
        return MarkerOptions().position(selectedProperty.value?.location!!)
    }

    fun updateSelectedProperty(members: List<TeamMember>?) {
        val newData = members?.find { it.email == selectedProperty.value?.email }
        newData?.let {
            _selectedProperty.value = it
        }
    }

    private val _selectedProperty = MutableLiveData<TeamMember>()
    val selectedProperty: LiveData<TeamMember>
        get() = _selectedProperty

    init {
        _selectedProperty.value = member
    }
    
}




