package com.sharp.githubuserprofile.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sharp.githubuserprofile.ui.screens.HomeScreen
import com.sharp.githubuserprofile.ui.screens.RepositoryDetailsScreen
import com.sharp.githubuserprofile.ui.screens.UserDetailsScreen
import com.sharp.githubuserprofile.viewmodel.UserInfoViewModel

@Composable
fun AppNavigationGraph(navController: NavHostController) {

    val userInfoViewModel: UserInfoViewModel = hiltViewModel()
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                onUserClick = { userId ->
                    navController.navigate(Screen.UserDetails.createRoute(userId))
                }, userInfoViewModel = userInfoViewModel
            )
            /*HomeScreen(onRepoClick = { repositoryId ->
                navController.navigate(Screen.RepositoryDetails.createRoute(repositoryId))
            }, userInfoViewModel = userInfoViewModel)*/
        }

        composable(
            route = Screen.UserDetails.route,
            arguments = Screen.UserDetails.navArguments
        ) { backStackEntry ->
            UserDetailsScreen(
                onClickBack = { navController.popBackStack() },
                onRepoClick = { repositoryId ->
                    navController.navigate(Screen.RepositoryDetails.createRoute(repositoryId))
                },
                userInfoViewModel = userInfoViewModel,
                backStackEntry.arguments?.getString("userId") ?: "",
            )
        }

        composable(
            route = Screen.RepositoryDetails.route,
            arguments = Screen.RepositoryDetails.navArguments
        ) { backStackEntry ->
            RepositoryDetailsScreen(
                onClickBack = { navController.popBackStack() },
                backStackEntry.arguments?.getInt("repositoryId") ?: 0,
                userInfoViewModel = userInfoViewModel
            )
        }
    }
}