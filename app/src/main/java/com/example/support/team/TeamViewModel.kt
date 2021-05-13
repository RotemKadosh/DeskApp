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

data class TeamMemberApiStatus(var isVisible: Boolean, @DrawableRes var imageResource: Int)

class TeamViewModel : ViewModel() {

    private val _status = MutableLiveData<TeamMemberApiStatus>()
    val status: LiveData<TeamMemberApiStatus>
        get() = _status

    private val _members = MutableLiveData<List<TeamMember>>()
    val members: LiveData<List<TeamMember>>
        get() = _members

    private val _navigateToSelectedMember= MutableLiveData<TeamMember?>()
    val navigateToSelectedMember: LiveData<TeamMember?>
        get() = _navigateToSelectedMember


    init {
        getTeamMembers()
    }

    private fun getTeamMembers() {
        viewModelScope.launch {
            _status.value = TeamMemberApiStatus(true, R.drawable.loading_animation)
            try {
                _members.value = MembersApi.retrofitService.getProperties()
                _status.value = TeamMemberApiStatus(false, R.drawable.loading_animation)
            } catch (e: Exception) {
                _status.value = TeamMemberApiStatus(true, R.drawable.ic_connection_error)
                _members.value = ArrayList()
                Log.e("rotem", e.message.toString())
            }
        }
    }

    fun displayMemberDetails(member: TeamMember) {
        _navigateToSelectedMember.value = member
    }

    fun displayMemberDetailsComplete() {
        _navigateToSelectedMember.value = null
    }





}
