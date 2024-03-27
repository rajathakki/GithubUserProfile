package com.sharp.githubuserprofile.data.local

import com.sharp.githubuserprofile.data.remote.models.responses.UserInfo
import com.sharp.githubuserprofile.data.remote.models.responses.UserRepos
import com.sharp.githubuserprofile.data.remote.models.responses.UserSearchResponse
import com.sharp.githubuserprofile.data.remote.models.responses.Users
import com.sharp.githubuserprofile.data.remote.service.ApiService
import retrofit2.Response
import javax.inject.Inject

class UserInfoDataSourceImpl @Inject constructor(private val apiService: ApiService) :
    UserInfoDataSource {
    override suspend fun getUserInfo(userId: String): Response<UserInfo> {
        return apiService.getUserInfo(userId)
    }

    override suspend fun getUserRepos(userId: String): Response<List<UserRepos>> {
        return apiService.getUserRepos(userId)
    }

    override suspend fun getUsersByName(userName: String): Response<UserSearchResponse> {
        return apiService.getUsersByName(userName)
    }
}