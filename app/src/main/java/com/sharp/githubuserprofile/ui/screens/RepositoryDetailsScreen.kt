package com.sharp.githubuserprofile.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.sharp.githubuserprofile.R
import com.sharp.githubuserprofile.data.remote.models.responses.UserRepos
import com.sharp.githubuserprofile.data.remote.service.ResourceState
import com.sharp.githubuserprofile.ui.components.EmptyState
import com.sharp.githubuserprofile.ui.theme.GithubUserProfileTheme
import com.sharp.githubuserprofile.utils.AppConstants.MINIMUM_FORKS_FOR_BADGE
import com.sharp.githubuserprofile.utils.AppUtilities.getFormattedDate
import com.sharp.githubuserprofile.viewmodel.UserInfoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryDetailsScreen(
    onClickBack: () -> Boolean,
    repositoryId: Int,
    userInfoViewModel: UserInfoViewModel
) {
    val repositoryList by userInfoViewModel.userRepositories.collectAsState()

    Scaffold(topBar = {
        TopAppBar(
            modifier = Modifier.statusBarsPadding(),
            title = {
                Text(
                    text = stringResource(R.string.repo_detail)
                )
            },
            navigationIcon = {
                IconButton(onClick = { onClickBack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

    }) { contentPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = contentPadding.calculateTopPadding())
        ) {
            when (repositoryList) {

                is ResourceState.Empty -> {}
                is ResourceState.Loading -> {}

                is ResourceState.Success -> {
                    val repositoryListData = (repositoryList as ResourceState.Success).data
                    val userRepos = repositoryListData?.find {
                        it.repositoryId == repositoryId
                    }
                    RepositoryDetails(userRepos)
                }

                is ResourceState.Error -> {
                    EmptyState()
                }
            }
        }
    }
}

@Composable
fun RepositoryDetails(userRepos: UserRepos?) {

    val showBadge = if ((userRepos?.forks ?: 0) >= MINIMUM_FORKS_FOR_BADGE) 1f else 0f

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.margin_10))
    ) {
        val (rawLayout, iconStarBadge, repositoryName, language, repoDescription) = createRefs()

        Text(
            text = userRepos?.name ?: "",
            modifier = Modifier
                .constrainAs(repositoryName) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    centerHorizontallyTo(parent)
                }
                .padding(top = dimensionResource(id = R.dimen.margin_5)),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Icon(
            modifier = Modifier
                .constrainAs(iconStarBadge) {
                    start.linkTo(repositoryName.end)
                    top.linkTo(repositoryName.top)
                    bottom.linkTo(repositoryName.bottom)
                }
                .padding(start = dimensionResource(id = R.dimen.margin_5))
                .alpha(showBadge),
            imageVector = Icons.Filled.Star,
            contentDescription = stringResource(R.string.label_star_icon),
            tint = Color.Red
        )

        Row(
            modifier = Modifier
                .constrainAs(rawLayout) {
                    start.linkTo(parent.start)
                    top.linkTo(repositoryName.bottom)
                    centerHorizontallyTo(parent)
                }
                .padding(top = dimensionResource(id = R.dimen.margin_10)),
        ) {
            Icon(
                Icons.Default.Build,
                modifier = Modifier.size(dimensionResource(id = R.dimen.margin_16)),
                contentDescription = stringResource(R.string.label_forks_icon)
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.margin_8)))
            Text(
                text = (userRepos?.forks ?: 0).toString(),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.margin_16)))
            Icon(
                Icons.Default.DateRange,
                modifier = Modifier.size(dimensionResource(id = R.dimen.margin_16)),
                contentDescription = stringResource(R.string.label_last_updated_icon)
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.margin_8)))
            Text(
                text = getFormattedDate(userRepos?.updatedAt ?: ""),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Text(
            text = userRepos?.language ?: "",
            modifier = Modifier
                .constrainAs(language) {
                    start.linkTo(parent.start)
                    top.linkTo(rawLayout.bottom)
                    bottom.linkTo(repoDescription.top)
                }
                .fillMaxWidth()
                .padding(
                    top = dimensionResource(id = R.dimen.margin_10)
                ),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Text(
            text = userRepos?.description ?: "",
            modifier = Modifier
                .constrainAs(repoDescription) {
                    start.linkTo(parent.start)
                    top.linkTo(language.bottom)
                }
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.margin_10)),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GithubUserProfileTheme {
        RepositoryDetails(UserRepos.sample())
    }
}