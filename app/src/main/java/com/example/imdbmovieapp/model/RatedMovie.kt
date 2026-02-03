package com.example.imdbmovieapp.model

import kotlinx.serialization.Serializable

@Serializable
data class RatedMovie(
    val imdbId: String,
    val title: String,
    val year: String,
    val poster: String,
    val userRating: Int
)