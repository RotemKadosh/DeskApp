package com.example.support.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.support.networking.TeamMember

class MapViewModelFactory(private val memberProperty: TeamMember) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(memberProperty) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}