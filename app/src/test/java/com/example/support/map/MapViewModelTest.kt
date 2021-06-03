package com.example.support.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.support.R
import com.example.support.getOrAwaitValue
import com.example.support.networking.TeamMember
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapViewModelTest {
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()



    val defaultMember = TeamMember("Rotem",
        "Kadosh",
        false,
        "0548166800",
        "rotem@gmail.com",
        "")

    lateinit var viewModel : MapViewModel

    @Before
    fun setUp() {
        viewModel = MapViewModel(defaultMember)
    }

    @Test
    fun getMemberTest() {

        Assert.assertNotNull(viewModel.selectedProperty)
        Assert.assertNotNull(viewModel.selectedProperty.value)
    }
    @Test
    fun updateSelectedPropertyTest(){

       val newMember =    TeamMember("yosi",
           "Ksh",
           true,
           "0548166800",
           "rotem@gmail.com",
           "")
       val newList = listOf<TeamMember>(newMember)
        viewModel.updateSelectedProperty(newList)
        var member =  viewModel.selectedProperty.getOrAwaitValue()
        Assert.assertEquals(member, newMember)

        val emptyList = listOf<TeamMember>()
        viewModel.updateSelectedProperty(emptyList)
        member =  viewModel.selectedProperty.getOrAwaitValue()
        Assert.assertEquals(member, newMember)

        val differentList = listOf<TeamMember>(
            TeamMember("yosi",
            "Ksh",
            true,
            "0548166800",
            "rotem@gmai",
            "")
        )
        viewModel.updateSelectedProperty(differentList)
        member =  viewModel.selectedProperty.getOrAwaitValue()
        Assert.assertEquals(member, newMember)
    }

    @Test
    fun getMarkerOptionTest(){
        val member = viewModel.selectedProperty.value
        var markerOptions : MarkerOptions
        if(member != null){
            markerOptions =  MarkerOptions().position(member.location)
                .title(member.firstName + " " + member.lastName)
                .snippet(member.phone)

        }else{
            markerOptions = MarkerOptions()
        }
        Assert.assertEquals(markerOptions.position, viewModel.getMarkerOption().position)
        Assert.assertEquals(markerOptions.title, viewModel.getMarkerOption().title)
        Assert.assertEquals(markerOptions.snippet, viewModel.getMarkerOption().snippet)
    }
    @Test
    fun ismarkerShowenTest(){
        var isShown = viewModel.isMarkerShown.getOrAwaitValue()
        Assert.assertEquals(isShown, true)
        var resource = viewModel.isShowenButtonResource.getOrAwaitValue()
        Assert.assertEquals(resource, R.drawable.ic_baseline_visibility_off)

        viewModel.onCangeVisibilityClicked()
        isShown = viewModel.isMarkerShown.getOrAwaitValue()
        Assert.assertEquals(isShown, false)
        resource = viewModel.isShowenButtonResource.getOrAwaitValue()
        Assert.assertEquals(resource, R.drawable.ic_baseline_visibility)
    }
}