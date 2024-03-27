package com.sharp.githubuserprofile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun GithubUserProfileApp() {
    val navController = rememberNavController()
    AppNavigationGraph(navController = navController)
}