package com.example.support

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
        val member = viewModel.members.getOrAwaitValue()[0]
        Assert.assertNotNull(member)
        Assert.assertEquals(member.firstName, "Peter")
    }


}