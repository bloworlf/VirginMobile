package com.example.virginmoney.data.repo

import com.example.virginmoney.data.models.people.PeopleModel
import com.example.virginmoney.data.models.room.RoomModel
import com.example.virginmoney.data.network.PeopleCall
import com.example.virginmoney.data.network.RoomsCall
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getRooms() = runTest {
        Mockito.`when`(roomsCall.getRooms()).thenReturn(
            arrayListOf(
                RoomModel(
                    createdAt = "2022-01-24T20:52:50.765Z",
                    isOccupied = false,
                    maxOccupancy = 34072,
                    id = 2
                )
            )
        )
        assertEquals(roomsCall.getRooms(), repo.getRooms())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getRooms_Empty() = runTest {
        Mockito.`when`(roomsCall.getRooms()).thenReturn(arrayListOf())
        assertEquals(0, repo.getRooms().size)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getRooms_Null() = runTest {
        Mockito.`when`(roomsCall.getRooms()).thenReturn(null)
        assertNull(repo.getRooms())
    }
}