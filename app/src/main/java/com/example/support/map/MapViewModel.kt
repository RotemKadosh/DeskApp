package com.example.support.map

import android.util.Log
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
        return MarkerOptions().position(selectedProperty.value?.location!!)
    }

    fun updateSelectedProperty(members: List<TeamMember>?) {
        val newData = members?.find { it.email == selectedProperty.value?.email }
        newData?.let {
            _selectedProperty.value = it
        }
    }


    val fullName =Transformations.map(selectedProperty){
        it.firstName + " " + it.lastName
    }
    val email = Transformations.map(selectedProperty){
        it.email
    }

    private val _selectedProperty = MutableLiveData<TeamMember>()
    val selectedProperty: LiveData<TeamMember>
        get() = _selectedProperty


    val infoResource = Transformations.map(_selectedProperty){
        if(it.available){
            R.drawable.ic_baseline_available_full
        }
        else{
            R.drawable.ic_baseline_block_full
        }
    }

    private val _isMarkerShown = MutableLiveData<Boolean>()
    val isMarkerShown: LiveData<Boolean>
        get() = _isMarkerShown

    init {
        _selectedProperty.value = member
        _isMarkerShown.value = true

    }

    val isShowenButtonResource = Transformations.map(_isMarkerShown){
        if(it){
                Log.d("icon", "Transformations")
                R.drawable.ic_baseline_visibility_off
        }
        else{
            R.drawable.ic_baseline_visibility
        }
    }

    fun onButtonclicked() {
        Log.d("icon", "onButtonclicked - before: " + _isMarkerShown.value.toString())
        _isMarkerShown.postValue(_isMarkerShown.value != true)
        Log.d("icon", "onButtonclicked - after: " + _isMarkerShown.value.toString())
    }
}




