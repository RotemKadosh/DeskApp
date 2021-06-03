package com.example.support.team

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.support.getOrAwaitValue
import com.example.support.networking.TeamMember
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class TeamViewModelTest{

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: TeamViewModel
    private val defaultMember = TeamMember("Rotem",
        "Kadosh",
        false,
        "0548166800",
        "rotem@gmail.com",
        "")
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
    @Test
    fun displayMemberDetailsTest(){
        viewModel.displayMemberDetails(defaultMember)
        val member = viewModel.navigateToSelectedMember.getOrAwaitValue()
        Assert.assertEquals(member, defaultMember)
    }
    @Test
    fun  displayMemberDetailsCompleteTest()
    {
        displayMemberDetailsTest()
        viewModel.displayMemberDetailsComplete()
        val member = viewModel.navigateToSelectedMember.getOrAwaitValue()
        Assert.assertNull(member)
    }
}