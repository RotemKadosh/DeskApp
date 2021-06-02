package com.example.support.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.support.R
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
        val member = selectedProperty.value
        var markerOptions : MarkerOptions
        if(member != null){
            markerOptions =  MarkerOptions().position(member.location)
                .title(member.firstName + " " + member.lastName)
                .snippet(member.phone)
                //.anchor(0.5f ,0.0f)
        }else{
            markerOptions = MarkerOptions()
        }
        return markerOptions
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

    private val _isMarkerShown = MutableLiveData<Boolean>()
    val isMarkerShown: LiveData<Boolean>
        get() = _isMarkerShown

    init {
        _selectedProperty.value = member
        _isMarkerShown.value = true

    }

    val isShowenButtonResource = Transformations.map(_isMarkerShown){
        when(it){
            true -> R.drawable.ic_baseline_visibility_off
            else -> R.drawable.ic_baseline_visibility
        }
    }

    fun onCangeVisibilityClicked() {
        _isMarkerShown.postValue(_isMarkerShown.value != true)
    }
}




