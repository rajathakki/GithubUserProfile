package com.sharp.githubuserprofile.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sharp.githubuserprofile.R
import com.sharp.githubuserprofile.ui.theme.GithubUserProfileTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(@StringRes title: Int, modifier: Modifier = Modifier) {
    TopAppBar(
        modifier = modifier.statusBarsPadding(),
        title = {
            Text(
                modifier = Modifier.padding(horizontal = 15.dp),
                text = stringResource(id = title),
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    GithubUserProfileTheme {
        TopBar(R.string.app_name)
    }
}