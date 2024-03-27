package com.sharp.githubuserprofile.data.remote.service

import com.sharp.githubuserprofile.data.remote.models.responses.UserInfo
import com.sharp.githubuserprofile.data.remote.models.responses.UserRepos
import com.sharp.githubuserprofile.data.remote.models.responses.UserSearchResponse
import com.sharp.githubuserprofile.utils.MockResponseFileReader
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.HttpURLConnection

class ApiServiceTest {

    private lateinit var mockServer: MockWebServer
    private lateinit var apiService: ApiService
    private lateinit var moshi: Moshi

    @Before
    fun setUp() {
        mockServer = MockWebServer()
        mockServer.start()
        moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        apiService = Retrofit.Builder()
            .baseUrl(mockServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build().create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }

    @Test
    fun `getUsersList() returns Success`() = runTest {

        // Assign
        val mockedResponse = MockResponseFileReader("users_list_success.json").content

        mockServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(mockedResponse) //add it in the server response queue
        )

        // Test the function (Act)
        val response = apiService.getUsersByName("vivek")
        val adapter: JsonAdapter<UserSearchResponse> = moshi.adapter(UserSearchResponse::class.java)
        val json = adapter.toJson(response.body())
        val resultResponse = adapter.fromJson(json)
        val expectedResponse = adapter.fromJson(mockedResponse)

        // Verify the result (Assert)
        assertNotNull(response)
        assertEquals(resultResponse, expectedResponse)
    }

    @Test
    fun `getUserInfo() returns Success`() = runTest {

        // Assign
        val mockedResponse = MockResponseFileReader("user_info_success.json").content

        mockServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(mockedResponse) //add it in the server response queue
        )

        // Test the function (Act)
        val response = apiService.getUserInfo("testUserId")
        val adapter: JsonAdapter<UserInfo> = moshi.adapter(UserInfo::class.java)
        val json = adapter.toJson(response.body())
        val resultResponse = adapter.fromJson(json)
        val expectedResponse = adapter.fromJson(mockedResponse)

        // Verify the result (Assert)
        assertNotNull(response)
        assertEquals(resultResponse, expectedResponse)
    }

    @Test
    fun `getUserRepos() returns Success`() = runTest {

        // Assign
        val mockedResponse = MockResponseFileReader("user_repos_success.json").content

        mockServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(mockedResponse) //add it in the server response queue
        )

        // Test the function (Act)
        val response = apiService.getUserRepos("testUserId")
        val adapter: JsonAdapter<List<UserRepos>> =
            moshi.adapter(Types.newParameterizedType(List::class.java, UserRepos::class.java))
        val json = adapter.toJson(response.body())
        val resultResponse = adapter.fromJson(json)
        val expectedResponse = adapter.fromJson(mockedResponse)

        // Verify the result (Assert)
        assertNotNull(response)
        assertEquals(resultResponse, expectedResponse)
    }

}