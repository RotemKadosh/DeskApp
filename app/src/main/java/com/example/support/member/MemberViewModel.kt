package com.example.support.member

import androidx.lifecycle.*
import com.example.support.R
import com.example.support.networking.TeamMember


class MemberViewModel( memberProperty: TeamMember) : ViewModel() {
    fun updateSelectedProperty(members: List<TeamMember>?) {
        val newData  = members?.find{it.email == selectedProperty.value?.email}
        newData?.let{
            _selectedProperty.value = it
        }
    }


    private val _selectedProperty = MutableLiveData<TeamMember>()
    val selectedProperty: LiveData<TeamMember>
        get() = _selectedProperty

    init {
        _selectedProperty.value = memberProperty
    }

    val displayAvailability = Transformations.map(selectedProperty) {
        return@map when (it.available) {
            true -> R.drawable.ic_baseline_available
            else -> R.drawable.ic_baseline_block
        }
    }

    val displayFullName = Transformations.map(selectedProperty) {
        return@map _selectedProperty.value?.firstName + " " + selectedProperty.value?.lastName
    }
    
}

