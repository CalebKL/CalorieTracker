package com.example.tracker_data.repository

import com.example.tracker_data.local.TrackerDao
import com.example.tracker_data.remote.OpenFoodApi
import com.example.tracker_data.remote.malformedFoodResponse
import com.example.tracker_data.remote.validFoodResponse
import com.google.common.truth.Truth
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


class TrackerRepositoryImpTest {
    private var dao = mockk<TrackerDao>(relaxed = true)
    private lateinit var repository: TrackerRepositoryImp
    private lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var api: OpenFoodApi
    @Before
    fun setup(){
        mockWebServer = MockWebServer()
        okHttpClient = OkHttpClient.Builder()
            .writeTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .connectTimeout(1, TimeUnit.SECONDS)
            .build()
        api = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(mockWebServer.url("/"))
            .build()
            .create(OpenFoodApi::class.java)
        repository = TrackerRepositoryImp(
            dao =dao,
            api = api
        )
    }
    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }

    @Test
    fun `Search food, valid response, returns results`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(validFoodResponse)
        )
        val result = repository.searchFood("banana", 1, 40)
        Truth.assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `Search food, invalid response, returns failure`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(403)
                .setBody(validFoodResponse)
        )
        val result = repository.searchFood("banana", 1, 40)
        Truth.assertThat(result.isFailure).isTrue()
    }
    @Test
    fun `Search food, malformed response, returns failure`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(malformedFoodResponse)
        )
        val result = repository.searchFood("banana", 1, 40)
        Truth.assertThat(result.isFailure).isTrue()
    }
}