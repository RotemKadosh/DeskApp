package com.example.support.map

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _selectedProperty = MutableLiveData<TeamMember>()
    val selectedProperty: LiveData<TeamMember>
        get() = _selectedProperty

    init {
        _selectedProperty.value = member
    }


    fun render(view: View) {
        val availabilityImageResource =
            when (this@MapViewModel._selectedProperty.value?.available) {
                true -> R.drawable.ic_baseline_available
                else -> R.drawable.ic_baseline_block
            }

        view.findViewById<ImageView>(R.id.infoWindowImage)
            .setImageResource(availabilityImageResource)

        val titleUi = view.findViewById<TextView>(R.id.title)
        val title = with(this@MapViewModel.selectedProperty) {
            value?.firstName + " " + value?.lastName
        }
        titleUi.text = title

        val snippet: String? = this@MapViewModel._selectedProperty.value?.email
        val snippetUi = view.findViewById<TextView>(R.id.snippet)
        snippetUi.text = snippet
    }
}




