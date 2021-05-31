package com.example.support.member

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.support.R
import com.example.support.getOrAwaitValue
import com.example.support.networking.TeamMember
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
class MemberViewModelTest {
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()



    var defaultMember = TeamMember("Rotem",
    "Kadosh",
        false,
    "0548166800",
    "rotem@gmail.com",
    "")

    lateinit var viewModel : MemberViewModel

    @Before
    fun setUp() {
        viewModel = MemberViewModel(defaultMember)
    }

    @Test
    fun getMemberTest() {

        Assert.assertNotNull(viewModel.selectedProperty)
        Assert.assertNotNull(viewModel.selectedProperty.value)
    }
    @Test
    fun displayFullNameTest(){
        val fullName : String = viewModel.displayFullName.getOrAwaitValue()
        Assert.assertEquals(fullName, "Rotem Kadosh")
    }
    @Test
    fun displayAvailabilityTest(){
        val availabilityResId = viewModel.displayAvailability.getOrAwaitValue()
        Assert.assertEquals(availabilityResId, R.drawable.ic_baseline_block)
    }
    @Test
    fun updateSelectedPropertyTest(){
        val newList = listOf<TeamMember>(TeamMember("yosi",
            "Ksh",
            true,
            "0548166800",
            "rotem@gmail.com",
            "")
        )
        viewModel.updateSelectedProperty(newList)
        val fullName : String = viewModel.displayFullName.getOrAwaitValue()
        Assert.assertEquals(fullName, "yosi Ksh")

        val availabilityResId = viewModel.displayAvailability.getOrAwaitValue()
        Assert.assertEquals(availabilityResId, R.drawable.ic_baseline_available)
    }
}