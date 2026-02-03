package com.example.imdbmovieapp.screens

sealed class Screen(val route: String) {
    data object Search : Screen(route = "search")
    data object Detail : Screen(route = "detail/{imdbId}") {
        fun createRoute(imdbId: String) = "detail/$imdbId"
    }
}