package com.example.virginmoney.data.repo

import com.example.virginmoney.models.people.PeopleModel
import com.example.virginmoney.network.PeopleCall
import com.example.virginmoney.network.RoomsCall
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class RepositoryImplTest {

    lateinit var repo: Repository

    @Mock
    lateinit var peopleCall: PeopleCall

    @Mock
    lateinit var roomsCall: RoomsCall

    @Before
    fun onStart() {
        MockitoAnnotations.openMocks(this)

        repo = RepositoryImpl(peopleCall, roomsCall)
    }

    @After
    fun onFinish() {
        Mockito.clearAllCaches()
    }


    @Test
    fun getPeople() = runTest {
        Mockito.`when`(peopleCall.getPeople()).thenReturn(
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
        assertEquals(peopleCall.getPeople(), repo.getPeople())
    }

    @Test
    fun getPeople_Empty() = runTest {
        Mockito.`when`(peopleCall.getPeople()).thenReturn(arrayListOf())

        assertEquals(0, repo.getPeople().size)
    }

    @Test
    fun getPeople_Null() = runTest {
        Mockito.`when`(peopleCall.getPeople()).thenReturn(null)

        assertNull(repo.getPeople())
    }

    @Test
    fun getRooms() {
    }
}