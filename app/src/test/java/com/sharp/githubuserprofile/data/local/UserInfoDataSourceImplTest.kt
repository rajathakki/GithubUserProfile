package com.sharp.githubuserprofile.data.local

import com.sharp.githubuserprofile.data.remote.models.responses.UserInfo
import com.sharp.githubuserprofile.data.remote.models.responses.UserRepos
import com.sharp.githubuserprofile.data.remote.service.ApiService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class UserInfoDataSourceImplTest {

    @MockK
    private lateinit var apiService: ApiService

    private lateinit var userInfoDataSourceImpl: UserInfoDataSourceImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        userInfoDataSourceImpl = UserInfoDataSourceImpl(apiService)
    }

    @Test
    fun `getUserInfo returns result`() = runTest {

        //Assign
        val userInfo = UserInfo(name = "Test", avatarUrl = "Test")

        coEvery { apiService.getUserInfo("testUserId") } returns Response.success(userInfo)

        //Act
        val result = userInfoDataSourceImpl.getUserInfo("testUserId")

        //Assert
        TestCase.assertNotNull(result)
    }

    @Test
    fun `getUserRepos returns result`() = runTest {

        //Assign
        val userRepos = listOf(UserRepos.sample(), UserRepos.sample(), UserRepos.sample())

        coEvery { apiService.getUserRepos("testUserId") } returns Response.success(userRepos)

        //Act
        val result = userInfoDataSourceImpl.getUserRepos("testUserId")

        //Assert
        TestCase.assertNotNull(result)

    }
}