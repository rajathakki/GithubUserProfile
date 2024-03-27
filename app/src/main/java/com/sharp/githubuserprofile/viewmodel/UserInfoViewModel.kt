package com.sharp.githubuserprofile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharp.githubuserprofile.data.remote.models.responses.UserInfo
import com.sharp.githubuserprofile.data.remote.models.responses.UserRepos
import com.sharp.githubuserprofile.data.remote.models.responses.UserSearchResponse
import com.sharp.githubuserprofile.data.remote.repository.UserInfoRepository
import com.sharp.githubuserprofile.data.remote.service.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {

    private val _users: MutableStateFlow<ResourceState<UserSearchResponse?>> =
        MutableStateFlow(ResourceState.Empty())
    private val _userInfo: MutableStateFlow<ResourceState<UserInfo?>> =
        MutableStateFlow(ResourceState.Empty())
    private val _userRepositories: MutableStateFlow<ResourceState<List<UserRepos>?>> =
        MutableStateFlow(ResourceState.Empty())

    val users: StateFlow<ResourceState<UserSearchResponse?>> = _users

    val userInfo: StateFlow<ResourceState<UserInfo?>> = _userInfo

    val userRepositories: StateFlow<ResourceState<List<UserRepos>?>> = _userRepositories

    /**
     * Fetches user information for the specified [userId] and updates the [_userInfo].
     * It observes the result using [ResourceState] to handle success and error states.
     */
    fun getUserInfo(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userInfoRepository.getUserInfo(userId).collectLatest { userInfo ->
                _userInfo.value = userInfo
            }
        }
    }

    /**
     * Fetches user repository details for the specified [userId] and updates the [_userRepositories].
     * It observes the result using [ResourceState] to handle success and error states.
     */
    fun getUserReposDetails(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userInfoRepository.getUserReposDetails(userId).collectLatest { repositories ->
                _userRepositories.value = repositories
                // sortReposListByForks(repositories)
            }
        }
    }

    fun getUsers(userName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userInfoRepository.getUsersByName(userName).collectLatest { users ->
                _users.value = users
            }
        }
    }

    fun sortReposListByForks() {
        val value = _userRepositories.value
        val repositoryListData = (value as ResourceState.Success).data
        val userRepos = repositoryListData?.sortedByDescending { it.forks }
        _userRepositories.value = ResourceState.Success(userRepos)
    }

    fun sortReposListByLastUpdated() {
        val value = _userRepositories.value
        val repositoryListData = (value as ResourceState.Success).data
        val userRepos = repositoryListData?.sortedByDescending { it.updatedAt }
        _userRepositories.value = ResourceState.Success(userRepos)
    }

}