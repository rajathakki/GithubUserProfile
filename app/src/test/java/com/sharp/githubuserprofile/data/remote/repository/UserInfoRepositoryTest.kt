package com.sharp.githubuserprofile.data.remote.repository

import com.sharp.githubuserprofile.data.local.UserInfoDataSource
import com.sharp.githubuserprofile.data.remote.models.responses.UserInfo
import com.sharp.githubuserprofile.data.remote.models.responses.UserRepos
import com.sharp.githubuserprofile.data.remote.service.ResourceState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class UserInfoRepositoryTest {

    @MockK
    private lateinit var userInfoDataSource: UserInfoDataSource

    private lateinit var userInfoRepository: UserInfoRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        userInfoRepository = UserInfoRepository(userInfoDataSource)
    }

    @Test
    fun `getUserInfo() should return flow when user is found`() {

        // Mocking the response from the service (Assign)
        coEvery { userInfoDataSource.getUserInfo("testUserId") } returns Response.success(
            mockUserInfo()
        )

        // Collect the flow
        runTest {
            //Act
            val result = userInfoRepository.getUserInfo("testUserId")

            //Assert
            TestCase.assertNotNull(result)
        }
    }

    @Test
    fun `getUserInfo() should return error state when exception occurs`() = runTest {

        // Mocking an exception being thrown from the service
        coEvery { userInfoDataSource.getUserInfo("testUserId") } throws Exception("Test exception")

        // Call the repository function and assert the result
        val flow = userInfoRepository.getUserInfo("testUserId")
        val result = flow.drop(1).first()
        // Verify api service method call
        coVerify { userInfoDataSource.getUserInfo("testUserId") }

        // Asserting that the result is of type Error and contains expected error message
        assertEquals(ResourceState.Error<UserInfo>("Test exception"), result)
    }

    @Test
    fun `getUserRepos() should return flow when user repos found`() {

        // Mocking the response from the service (Assign)
        coEvery { userInfoDataSource.getUserRepos("testUserId") } returns Response.success(
            mockUserRepos()
        )

        // Collect the flow
        runTest {
            //Act
            val result = userInfoRepository.getUserReposDetails("testUserId")

            //Assert
            TestCase.assertNotNull(result)
        }

    }

    @Test
    fun `getUserRepos() should return an empty flow when there are no repos`() {

        // Mocking the response from the service (Assign)
        coEvery { userInfoDataSource.getUserRepos("testUserId") } returns Response.success(
            emptyList()
        )

        // Collect the flow
        runTest {
            //Act
            val result = userInfoRepository.getUserReposDetails("testUserId")

            //Assert
            TestCase.assertNotNull(result)
        }
    }

    @Test
    fun `getUserRepos() should return error state when exception occurs`() = runTest {

        // Mocking an exception being thrown from the service
        coEvery { userInfoDataSource.getUserRepos("testUserId") } throws Exception("Test exception")

        // Call the repository function and assert the result
        val flow = userInfoRepository.getUserReposDetails("testUserId")
        val result = flow.drop(1).first()
        // Verify api service method call
        coVerify { userInfoDataSource.getUserRepos("testUserId") }

        // Asserting that the result is of type Error and contains expected error message
        assertEquals(ResourceState.Error<List<UserRepos>>("Test exception"), result)
    }

    private fun mockUserInfo(): UserInfo {
        return UserInfo(name = "Test User", avatarUrl = "https://test.com/avatar.jpg")
    }

    private fun mockUserRepos(): List<UserRepos> {
        return listOf(
            UserRepos.sample(),
            UserRepos.sample(),
        )
    }
}