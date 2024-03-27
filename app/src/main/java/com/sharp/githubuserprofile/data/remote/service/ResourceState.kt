package com.sharp.githubuserprofile.data.remote.service

sealed class ResourceState<T> {
    class Empty<T> : ResourceState<T>()
    class Loading<T> : ResourceState<T>()
    data class Success<T>(val data: T) : ResourceState<T>()
    data class Error<T>(val errorMessage: String) : ResourceState<T>()
}