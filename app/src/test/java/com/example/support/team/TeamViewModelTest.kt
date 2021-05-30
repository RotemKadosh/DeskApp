package com.example.support.team

import com.example.support.networking.TeamMember
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class TeamViewModelTest{


    private lateinit var viewModel: TeamViewModel

    @Before
    fun setUp(){
        viewModel = TeamViewModel()
    }
    @Test
    fun updateSearchTextTest(){
        viewModel.updateSearchText("")
        var text = viewModel.searchText
        Assert.assertNull(text)
        viewModel.updateSearchText("rotem")
        text = viewModel.searchText
        Assert.assertEquals(text, "rotem")
    }
    fun displayMemberDetails(){
        viewModel.displayMemberDetails(TeamMember("rotem",
        ""))
    }
}