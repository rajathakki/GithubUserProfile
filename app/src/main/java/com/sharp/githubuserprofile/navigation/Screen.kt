package com.sharp.githubuserprofile.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    data object Home : Screen(route = "Home")

    data object UserDetails : Screen(
        route = "userDetail/{userId}",
        navArguments = listOf(navArgument(name = "userId") {
            type = NavType.StringType
        })
    ) {
        fun createRoute(userId: String) = "userDetail/${userId}"
    }
    data object RepositoryDetails : Screen(
        route = "repositoryDetail/{repositoryId}",
        navArguments = listOf(navArgument(name = "repositoryId") {
            type = NavType.IntType
        })
    ) {
        fun createRoute(repositoryId: Int) = "repositoryDetail/${repositoryId}"
    }
}