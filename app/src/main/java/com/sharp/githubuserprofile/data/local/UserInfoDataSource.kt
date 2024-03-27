package com.sharp.githubuserprofile.data.local

import com.sharp.githubuserprofile.data.remote.models.responses.UserInfo
import com.sharp.githubuserprofile.data.remote.models.responses.UserRepos
import com.sharp.githubuserprofile.data.remote.models.responses.UserSearchResponse
import com.sharp.githubuserprofile.data.remote.models.responses.Users
import retrofit2.Response

interface UserInfoDataSource {
    suspend fun getUserInfo(userId: String): Response<UserInfo>
    suspend fun getUserRepos(userId: String): Response<List<UserRepos>>
    suspend fun getUsersByName(userName: String): Response<UserSearchResponse>
}