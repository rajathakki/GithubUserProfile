package com.sharp.githubuserprofile.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.dimensionResource
import com.sharp.githubuserprofile.R
import com.sharp.githubuserprofile.data.remote.models.responses.UserSearchResponse
import com.sharp.githubuserprofile.data.remote.models.responses.Users

@Composable
fun UsersList(users: UserSearchResponse, onUserClick: (String) -> Unit ={}) {
    val translationY = remember { Animatable(500f) }

    LaunchedEffect(Unit) {
        translationY.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 500)
        )
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = dimensionResource(id = R.dimen.margin_10),
                bottom = dimensionResource(id = R.dimen.margin_10)
            )
            .graphicsLayer(
                translationY = translationY.value
            ),
        state = rememberLazyListState()
    ) {
        if (users.items?.isNotEmpty() == true) {
            items(users.items) { users ->
                UserItem(users = users) { userId ->
                    onUserClick(userId)
                }
                /*RepositoryItem(userRepos = userRepos) { repositoryId ->
                    onRepoClick(repositoryId)
                }*/
            }
        }

    }
}