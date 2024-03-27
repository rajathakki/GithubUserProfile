package com.sharp.githubuserprofile.data.remote.repository

import com.sharp.githubuserprofile.data.local.UserInfoDataSource
import com.sharp.githubuserprofile.data.remote.models.responses.UserInfo
import com.sharp.githubuserprofile.data.remote.models.responses.UserRepos
import com.sharp.githubuserprofile.data.remote.models.responses.UserSearchResponse
import com.sharp.githubuserprofile.data.remote.models.responses.Users
import com.sharp.githubuserprofile.data.remote.service.ResourceState
import com.sharp.githubuserprofile.utils.AppConstants.DELAY
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Repository class responsible for fetching user information and repositories details
 * from the GitHub API.
 */
class UserInfoRepository @Inject constructor(private val userInfoDataSource: UserInfoDataSource) {

    /**
     * Fetches user information for the specified [userId] from the GitHub API.
     */
    suspend fun getUserInfo(
        userId: String
    ): Flow<ResourceState<UserInfo?>> {
        return flow {
            emit(ResourceState.Loading())

            val response = userInfoDataSource.getUserInfo(userId)
            if (response.isSuccessful && response.body() != null) {
                emit(ResourceState.Success(response.body()))
            } else {
                if (response.code() == 403) {
                    emit(ResourceState.Error("Something went wrong, please try after sometime"))
                }
                emit(ResourceState.Error("Error fetching data"))
            }
        }
            .catch { exception ->
                emit(ResourceState.Error(exception.localizedMessage ?: "Unknown error"))
            }
    }

    /**
     * Fetches user's public repositories details for the specified [userId] from the GitHub API.
     */
    suspend fun getUserReposDetails(
        userId: String
    ): Flow<ResourceState<List<UserRepos>?>> {

        return flow {

            emit(ResourceState.Loading())
            delay(DELAY)
            val response = userInfoDataSource.getUserRepos(userId)
            if (response.isSuccessful && response.body() != null) {
                emit(ResourceState.Success(response.body()))
            } else {
                if (response.code() == 403) {
                    emit(ResourceState.Error("Something went wrong, please try after sometime"))
                }
                emit(ResourceState.Error("Error fetching data"))
            }
        }
            .catch { exception ->
                emit(ResourceState.Error(exception.localizedMessage ?: "Unknown error"))
            }
    }

    suspend fun getUsersByName(
        userName: String
    ): Flow<ResourceState<UserSearchResponse?>> {
        return flow {
            emit(ResourceState.Loading())
            val response = userInfoDataSource.getUsersByName(userName)
            if (response.isSuccessful && response.body() != null) {
                emit(ResourceState.Success(response.body()))
            } else {
                if (response.code() == 403) {
                    emit(ResourceState.Error("Something went wrong, please try after sometime"))
                }
                emit(ResourceState.Error("Error fetching data"))
            }
        }
            .catch { exception ->
                emit(ResourceState.Error(exception.localizedMessage ?: "Unknown error"))
            }
    }

}