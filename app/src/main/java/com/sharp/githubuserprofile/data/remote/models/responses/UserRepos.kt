package com.sharp.githubuserprofile.data.remote.models.responses

import com.squareup.moshi.Json

/**
 * UserRepo model
 */
data class UserRepos(
    @Json(name = "id") val repositoryId: Int?,
    val name: String?,
    val description: String?,
    @Json(name = "updated_at") val updatedAt: String?,
    @Json(name = "stargazers_count") val count: String?,
    @Json(name = "language") val language: String?,
    val forks: Int?
) {
    companion object {
        /**
         *Creates a sample repo object for preview
         */
        fun sample() = UserRepos(
            repositoryId = 1,
            name = "Sample",
            description = "This is temporary description",
            forks = 20,
            updatedAt = null,
            language = "Javascript",
            count = "123"
        )
    }
}
