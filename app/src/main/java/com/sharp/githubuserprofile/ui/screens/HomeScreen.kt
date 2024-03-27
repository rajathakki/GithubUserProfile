package com.sharp.githubuserprofile.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.sharp.githubuserprofile.viewmodel.UserInfoViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.sharp.githubuserprofile.R
import com.sharp.githubuserprofile.data.remote.models.responses.UserInfo
import com.sharp.githubuserprofile.data.remote.models.responses.UserRepos
import com.sharp.githubuserprofile.data.remote.models.responses.UserSearchResponse
import com.sharp.githubuserprofile.data.remote.models.responses.Users
import com.sharp.githubuserprofile.data.remote.service.ResourceState
import com.sharp.githubuserprofile.ui.components.EmptyState
import com.sharp.githubuserprofile.ui.components.Loader
import com.sharp.githubuserprofile.ui.components.TopBar
import com.sharp.githubuserprofile.ui.theme.GithubUserProfileTheme
import com.sharp.githubuserprofile.utils.AppConstants.BUTTON_WEIGHT
import com.sharp.githubuserprofile.utils.AppConstants.TEXT_WEIGHT
import com.sharp.githubuserprofile.utils.AppUtilities

@Composable
fun HomeScreen(
    onUserClick: (String) -> Unit = {},
    userInfoViewModel: UserInfoViewModel,
) {
    val userInfo by userInfoViewModel.userInfo.collectAsState()
    val repositoryList by userInfoViewModel.userRepositories.collectAsState()
    val usersList by userInfoViewModel.users.collectAsState()

    MainContent(
        userInfoState = userInfo,
        repositoryListState = repositoryList,
        usersList = usersList,
        onSearchClick = { userId ->
            userInfoViewModel.getUsers(userId)
        },
        onUserClick = onUserClick
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainContent(
    userInfoState: ResourceState<UserInfo?>,
    repositoryListState: ResourceState<List<UserRepos>?>,
    usersList: ResourceState<UserSearchResponse?>,
    onSearchClick: (String) -> Unit,
    onUserClick: (String) -> Unit
) {

    var lastEnteredUserId by rememberSaveable { mutableStateOf("") }
    val controller = LocalSoftwareKeyboardController.current
    var userId by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = { TopBar(title = R.string.app_name) }) {
        Column(
            modifier = Modifier
                .padding(
                    start = dimensionResource(R.dimen.margin_10),
                    top = dimensionResource(R.dimen.margin_15)
                )
                .fillMaxWidth()
        ) {

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(modifier = Modifier.weight(TEXT_WEIGHT),
                    value = userId,
                    onValueChange = { userId = it },
                    label = { Text(stringResource(id = R.string.hint_user_id)) })
                Button(
                    enabled = userId.isNotEmpty(),
                    modifier = Modifier
                        .padding(
                            start = dimensionResource(R.dimen.margin_8),
                            top = dimensionResource(R.dimen.margin_15),
                            end = dimensionResource(R.dimen.margin_10)
                        )
                        .weight(BUTTON_WEIGHT),
                    onClick = {
                        controller?.hide()
                        if (AppUtilities.isValidString(userId)) {
                            if (userId.isNotEmpty() && userId != lastEnteredUserId) {
                                lastEnteredUserId = userId
                                onSearchClick(userId.trim())
                                // onUserClick(userId.trim())
                            }
                        } else {
                            Toast.makeText(
                                context, R.string.error_message, Toast.LENGTH_SHORT
                            ).show()
                        }

                    }) {
                    Text(
                        text = stringResource(R.string.search).uppercase(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            when (usersList) {
                is ResourceState.Empty -> {}
                is ResourceState.Loading -> {
                    Loader()
                }

                is ResourceState.Success -> {
                    val usersListData = usersList.data
                    if (usersListData?.items?.isNotEmpty() == true) {
                        UsersList(usersListData, onUserClick)
                    } else {
                        EmptyState()
                    }
                }

                is ResourceState.Error -> {
                    EmptyState()
                }
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainContentPreview() {
    GithubUserProfileTheme {
        MainContent(
            userInfoState =
            ResourceState.Success(
                UserInfo(
                    name = "Test",
                    avatarUrl = "https://avatars3.githubusercontent.com/u/583231?v=4"
                )
            ),
            usersList = ResourceState.Success(null),
            repositoryListState = ResourceState.Success(listOf(UserRepos.sample())),
            onSearchClick = {},
            onUserClick = {}
        )
    }
}