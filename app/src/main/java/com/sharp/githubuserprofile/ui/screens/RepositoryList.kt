package com.sharp.githubuserprofile.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import com.sharp.githubuserprofile.R
import com.sharp.githubuserprofile.data.remote.models.responses.UserRepos

@Composable
fun RepositoryList(userRepos: List<UserRepos?>, onRepoClick: (Int) -> Unit,
                   onSortClick: () -> Unit) {
    val translationY = remember { Animatable(500f) }

    LaunchedEffect(Unit) {
        translationY.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 500)
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = dimensionResource(id = R.dimen.margin_10),
                bottom = dimensionResource(id = R.dimen.margin_10))
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .clickable { onSortClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Sort By",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.margin_5))
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
            items(userRepos) { userRepos ->
                RepositoryItem(userRepos = userRepos) { repositoryId ->
                    onRepoClick(repositoryId)
                }
            }
        }
    }
}