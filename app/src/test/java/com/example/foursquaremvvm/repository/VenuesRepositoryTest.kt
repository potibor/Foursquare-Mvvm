package com.example.foursquaremvvm.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.foursquaremvvm.data.local.VenueLocalDataSource
import com.example.foursquaremvvm.data.remote.datasource.VenueRemoteDataSource
import com.example.foursquaremvvm.data.remote.model.FoursquareModel
import com.example.foursquaremvvm.data.remote.model.LocationModel
import com.example.foursquaremvvm.data.remote.model.ResponseModel
import com.example.foursquaremvvm.data.remote.model.VenueModel
import com.example.foursquaremvvm.data.repository.VenueRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.assertj.core.api.Assertions.assertThat


@RunWith(JUnit4::class)
class VenuesRepositoryTest {

    @get:Rule
    val testInstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var venueRemoteDataSource: VenueRemoteDataSource

    @RelaxedMockK
    lateinit var venueLocalDataSource: VenueLocalDataSource

    @InjectMockKs
    lateinit var venuesRepository: VenueRepository

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun `add venue list to local when called addVenuesToLocal`() {
        runBlocking {
            venuesRepository.addVenuesToLocal(mockVenueList())

            coVerify {
                venueLocalDataSource.addVenueList(mockVenueList())
            }
        }
    }

    @Test
    fun `fetch venue list from local when called fetchLocalVenues`() {
        runBlocking {
            val expectedResult = mockVenueList()

            coEvery {
                venueLocalDataSource.getAll()
            } returns expectedResult

            val actualResult = venuesRepository.fetchLocalVenues()
            assertThat(actualResult.size).isEqualTo(2)
        }
    }

    @Test
    fun `remove venue list after fetching all venues from local`() {
        runBlocking {
            val expectedResult = listOf<VenueModel>()

            coEvery {
                venueLocalDataSource.removeAllVenuesFirst()
            } returns Unit

            coEvery {
                venueLocalDataSource.getAll()
            } returns expectedResult

            val actualResult = venuesRepository.fetchLocalVenues()

            assertThat(actualResult.size).isEqualTo(0)
        }
    }

    @Test
    fun `remove locations after calculating distance between current and local location`() {
        runBlocking {
            val locationTestModel = mockLocationModel()

            coEvery {
                venueLocalDataSource.removeLocationsFromLocal()
            } returns Unit

            coEvery {
                venueLocalDataSource.getLocation()
            } returns locationTestModel

            val actualResult = venuesRepository.getLatLngFromLocal()

            assertThat(actualResult?.lat).isEqualTo(0.0)
            assertThat(actualResult?.lng).isEqualTo(0.0)
        }
    }

    @Test
    fun `add new locationModel to local when addNewLatLngToLocal`() {
        runBlocking {
            val locationTestModel = mockLocationModel()

            coEvery {
                venueLocalDataSource.add(mockLocationModel())
            } returns Unit

            coEvery {
                venueLocalDataSource.getLocation()
            } returns locationTestModel

            val actualResult = venuesRepository.getLatLngFromLocal()

            assertThat(actualResult?.distance).isEqualTo(5)
        }
    }

    @Test
    fun `fetch venue list when distance above 200meters`() {
        runBlocking {
            val foursquareTestModel = mockFourSquareModel()
            val locationTestModel = mockLocationModel()

            coEvery {
                venueLocalDataSource.getLocation()
            } returns locationTestModel

            coEvery {
                venueLocalDataSource.removeLocationsFromLocal()
            } returns Unit

            coEvery {
                venueLocalDataSource.add(locationTestModel)
            } returns Unit

            coEvery {
                venueRemoteDataSource.fetchVenues("0.6", "0.6")
            } returns foursquareTestModel

            coEvery {
                venueLocalDataSource.addVenueList(foursquareTestModel.response.venues)
            } returns Unit

            val actualResult = venuesRepository.fetchVenues(0.6 , 0.6)

            assertThat(actualResult[0]?.location?.lat).isEqualTo(0.0)
        }
    }

    @Test
    fun `fetch venue list when distance under 200meters`() {
        runBlocking {
            val venueListTestModel = mockVenueList()
            val locationTestModel = mockLocationModel()

            coEvery {
                venueLocalDataSource.getLocation()
            } returns locationTestModel

            coEvery {
                venueLocalDataSource.removeLocationsFromLocal()
            } returns Unit

            coEvery {
                venueLocalDataSource.add(locationTestModel)
            } returns Unit

            coEvery {
                venueLocalDataSource.addVenueList(venueListTestModel)
            } returns Unit

            coEvery {
                venueLocalDataSource.getAll()
            } returns venueListTestModel

            val actualResult = venuesRepository.fetchVenues(0.0 , 0.0)

            assertThat(actualResult.size).isEqualTo(venueListTestModel.size)
        }
    }

    private fun mockFourSquareModel(
        responseModel: ResponseModel = mockResponseModel()
    ) = FoursquareModel(response = responseModel)

    private fun mockResponseModel(
        venueList: List<VenueModel> = mockVenueList()
    ) = ResponseModel(venues = venueList)

    private fun mockVenueModel(
        name: String = "",
        locationModel: LocationModel = mockLocationModel(0.0, 0.0)
    ) = VenueModel(name, locationModel)

    @OptIn(ExperimentalStdlibApi::class)
    private fun mockVenueList(
        count: Int = 2,
        locationModel: LocationModel = mockLocationModel(0.0, 0.0)
    ) = buildList {
        repeat(count) {
            add(mockVenueModel(locationModel = locationModel))
        }
    }

    private fun mockLocationModel(
        lat: Double = 0.0,
        lng: Double = 0.0
    ) = LocationModel(
        id = 1,
        lat = lat,
        lng = lng,
        address = "",
        distance = 5
    )

}