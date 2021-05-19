package com.example.support.member

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.support.R
import com.example.support.networking.TeamMember


class MemberViewModel( memberProperty: TeamMember, app: Application) : AndroidViewModel(app) {


    private val _selectedProperty = MutableLiveData<TeamMember>()
    val selectedProperty: LiveData<TeamMember>
        get() = _selectedProperty

    private val _isAvailable = MutableLiveData<Boolean>()

    val isAvailable: LiveData<Boolean>
        get() = _isAvailable

    init {
        _selectedProperty.value = memberProperty
        _isAvailable.value = memberProperty.available
    }

    val displayAvailability = Transformations.map(selectedProperty) {
        return@map when (it.available) {
            true -> R.drawable.ic_baseline_available
            false -> R.drawable.ic_baseline_block
            else -> R.drawable.ic_baseline_block
        }
    }

    fun displayFullName(): String? {
        var fullName: String? = _selectedProperty.value?.firstName + " " +selectedProperty.value?.lastName
        return fullName
    }



}

