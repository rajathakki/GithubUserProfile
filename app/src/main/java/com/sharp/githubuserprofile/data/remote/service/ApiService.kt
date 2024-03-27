package com.sharp.githubuserprofile.data.remote.service

import com.sharp.githubuserprofile.data.remote.models.responses.UserInfo
import com.sharp.githubuserprofile.data.remote.models.responses.UserRepos
import com.sharp.githubuserprofile.data.remote.models.responses.UserSearchResponse
import com.sharp.githubuserprofile.utils.AppConstants.DEFAULT_PAGE
import com.sharp.githubuserprofile.utils.AppConstants.DEFAULT_PAGE_LIMIT
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    /**
     * Get users. Resturns a list of [UserSearchResponse] response
     */
    @GET("/search/users")
    suspend fun getUsersByName(@Query("q") login: String,
                               @Query("page") page: Int = DEFAULT_PAGE,
                               @Query("per_page") limit: Int = DEFAULT_PAGE_LIMIT) : Response<UserSearchResponse>

    /**
     *Get user info. Returns a [UserInfo] response.
     */
    @GET("users/{userId}")
    suspend fun getUserInfo(@Path(value = "userId") userId: String): Response<UserInfo>

    /**
     * Get user repos. Returns a list of [UserRepos] response.
     */
    @GET("users/{userId}/repos")
    suspend fun getUserRepos(@Path(value = "userId") userId: String): Response<List<UserRepos>>

}