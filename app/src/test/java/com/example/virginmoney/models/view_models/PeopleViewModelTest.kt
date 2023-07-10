package com.example.virginmoney.models.view_models

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.virginmoney.data.repo.RepositoryImpl
import com.example.virginmoney.models.people.PeopleModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class PeopleViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = StandardTestDispatcher()

    @Mock
    lateinit var repo: RepositoryImpl

    lateinit var viewModel: PeopleViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun onStart() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        viewModel = PeopleViewModel(repo)
    }

    @After
    fun onFinish() {
        Mockito.clearAllCaches()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getPeople() = runTest {
        Mockito.`when`(repo.getPeople()).thenReturn(
            arrayListOf(
                PeopleModel(
                    avatar = "https://randomuser.me/api/portraits/women/21.jpg",
                    createdAt = "2022-01-24T17:02:23.729Z",
                    data = null,
                    email = "Crystel.Nicolas61@hotmail.com",
                    favouriteColor = "pink",
                    firstName = "Maggie",
                    lastName = "Brekke",
                    id = 1,
                    jobtitle = "Future Functionality Strategist",
                    fromName = null,
                    to = null,
                    size = null,
                    type = null,
                    name = null,
                    x = null
                )
            )
        )

        viewModel.getPeople()

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(repo.getPeople(), viewModel.liveData.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getPeople_Empty() = runTest {
        Mockito.`when`(repo.getPeople()).thenReturn(arrayListOf())

        viewModel.getPeople()

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(0, viewModel.liveData.value?.size)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getPeople_Null() = runTest {
        Mockito.`when`(repo.getPeople()).thenReturn(null)

        viewModel.getPeople()

        testDispatcher.scheduler.advanceUntilIdle()

        assertNull(viewModel.liveData.value)
    }
}