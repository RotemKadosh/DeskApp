package com.example.support

import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.support.networking.MembersApi
import com.example.support.networking.TeamMember
import com.example.support.networking.TeamMemberApiService
import kotlinx.coroutines.launch
import org.jetbrains.annotations.TestOnly

data class TeamMemberApiStatus(var isVisible: Boolean, @DrawableRes var imageResource: Int)

class RefreshViewModel :ViewModel(){

    private val _status = MutableLiveData<TeamMemberApiStatus>()
    val status: LiveData<TeamMemberApiStatus>
        get() = _status

    private val _members = MutableLiveData<List<TeamMember>>()
    val members: LiveData<List<TeamMember>>
        get() = _members

    private fun getTeamMembers() {
        viewModelScope.launch {
            _status.value = TeamMemberApiStatus(true, R.drawable.loading_animation)
            try {
                val list : MutableList<TeamMember> = MembersApi.retrofitService.getProperties() as MutableList<TeamMember>
                list.sortByDescending { it.available }
                _members.value = list
                _status.value = TeamMemberApiStatus(false, R.drawable.loading_animation)
            } catch (e: Exception) {
                _status.value = TeamMemberApiStatus(true, R.drawable.ic_connection_error)
                _members.value = ArrayList()
            }
        }
    }

    fun refreshData(){
         getTeamMembers()
    }

    @TestOnly
    fun refreshDataTest(apiService: TeamMemberApiService ){
        viewModelScope.launch {
            _status.value = TeamMemberApiStatus(true, R.drawable.loading_animation)
            try {
                val list : MutableList<TeamMember> = apiService.getProperties() as MutableList<TeamMember>
                list.sortByDescending { it.available }
                _members.value = list
                _status.value = TeamMemberApiStatus(false, R.drawable.loading_animation)
            } catch (e: Exception) {
                _status.value = TeamMemberApiStatus(true, R.drawable.ic_connection_error)
                _members.value = ArrayList()
            }
        }
    }

}

