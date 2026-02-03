package com.example.imdbmovieapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.imdbmovieapp.screens.Screen
import com.example.imdbmovieapp.view.DetailScreen
import com.example.imdbmovieapp.view.SearchScreen


@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Search.route
    ) {
        composable(route = Screen.Search.route) {
            SearchScreen(
                onMovieClick = { imdbId ->
                    navController.navigate(Screen.Detail.createRoute(imdbId))
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("imdbId") { type = NavType.StringType }
            )
        ) {
            DetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}