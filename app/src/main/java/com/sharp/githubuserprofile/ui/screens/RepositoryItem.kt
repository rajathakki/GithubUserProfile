package com.sharp.githubuserprofile.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.sharp.githubuserprofile.R
import com.sharp.githubuserprofile.data.remote.models.responses.UserRepos
import com.sharp.githubuserprofile.ui.theme.GithubUserProfileTheme

@Composable
fun RepositoryItem(userRepos: UserRepos?, onItemClick: (Int) -> Unit = {}) {
    Card(
        modifier = Modifier
            .padding(
                start = dimensionResource(id = R.dimen.margin_6),
                end = dimensionResource(id = R.dimen.margin_16),
                top = dimensionResource(id = R.dimen.margin_10)
            )
            .fillMaxWidth()
            .clickable {
                onItemClick(userRepos?.repositoryId ?: 0)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(id = R.dimen.margin_4)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RectangleShape

    ) {
        Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.margin_16))) {
            Text(
                text = userRepos?.name ?: "",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.margin_16)))

            Text(
                text = userRepos?.description ?: "", style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RepositoryItemPreview() {
    GithubUserProfileTheme {
        RepositoryItem(
            userRepos = UserRepos.sample(),
            onItemClick = {})
    }
}