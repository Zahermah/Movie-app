package com.example.imdbmovieapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    @SerialName("Search") val search: List<MovieSummary>? = null,
    @SerialName("Response") val response: String,
    @SerialName("Error") val error: String? = null
)

@Serializable
data class MovieSummary(
    @SerialName("Title") val title: String,
    @SerialName("Year") val year: String,
    @SerialName("imdbID") val imdbId: String,
    @SerialName("Type") val type: String,
    @SerialName("Poster") val poster: String
)