package com.example.support

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.support.networking.TeamMember
import com.example.support.networking.TeamMemberApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RefreshViewModelTest {
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var viewModel: RefreshViewModel

    @Before
    fun setupViewModel() {
        viewModel = RefreshViewModel()
        viewModel.refreshData()
    }

    @Test
    fun getMembersTest() {
        Assert.assertNotNull(viewModel.members)
    }

    @Test
    fun getMembersItemTest() {
        val members = viewModel.members.getOrAwaitValue()
        val member = members[0]
        Assert.assertNotNull(member)
        Assert.assertEquals(member.firstName, "Peter")
    }

    private lateinit var apiService : TeamMemberApiService

     private var listMock : List<TeamMember>? = null

    @Test
    fun statusSuccessTest(){
        apiService = mock(TeamMemberApiService::class.java)
        testCoroutineRule.runBlockingTest {
            `when`(apiService.getProperties()).thenReturn(getMockList(true))
            viewModel.refreshDataTest(apiService)
            var membersList = viewModel.members.getOrAwaitValue()
            Assert.assertEquals(membersList, listMock)
            Assert.assertEquals(viewModel.status.value?.isVisible, false)
        }
    }

    @Test
    fun statusFailTest(){
        apiService = mock(TeamMemberApiService::class.java)
        testCoroutineRule.runBlockingTest {
            `when`(apiService.getProperties()).thenReturn(getMockList(false))
            viewModel.refreshDataTest(apiService)
            var membersList = viewModel.members.getOrAwaitValue()
            Assert.assertEquals(viewModel.status.value?.isVisible, true)
        }
    }


    private fun getMockList(isSuccess:Boolean) : List<TeamMember>?{
        if(isSuccess){
            val member = TeamMember("yosi",
                "Ksh",
                true,
                "0548166800",
                "rotem@gmail.com",
                "")
            listMock = listOf(member)
        }
        return listMock
    }

}