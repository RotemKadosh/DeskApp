package com.example.support.team

import android.util.Log
import android.view.DragEvent
import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.support.R
import com.example.support.networking.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch

class TeamViewModel : ViewModel() {

    var searchText: String? = null

    private val _navigateToSelectedMember= MutableLiveData<TeamMember?>()
    val navigateToSelectedMember: LiveData<TeamMember?>
        get() = _navigateToSelectedMember


    fun displayMemberDetails(member: TeamMember) {
        _navigateToSelectedMember.value = member
    }

    fun displayMemberDetailsComplete() {
        _navigateToSelectedMember.value = null
    }





}
