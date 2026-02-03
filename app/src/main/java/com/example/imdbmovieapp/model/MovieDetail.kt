package com.example.imdbmovieapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetail(
    @SerialName("Title") val title: String,
    @SerialName("Year") val year: String,
    @SerialName("Rated") val rated: String,
    @SerialName("Runtime") val runtime: String,
    @SerialName("Genre") val genre: String,
    @SerialName("Director") val director: String,
    @SerialName("Actors") val actors: String,
    @SerialName("Plot") val plot: String,
    @SerialName("Poster") val poster: String,
    @SerialName("imdbRating") val imdbRating: String,
    @SerialName("imdbID") val imdbId: String,
    @SerialName("Ratings") val ratings: List<SourceRating> = emptyList(),
    @SerialName("Awards") val awards: String = "N/A",
    @SerialName("BoxOffice") val boxOffice: String = "N/A",
    @SerialName("Response") val response: String,
    @SerialName("Type") val type:String
)

@Serializable
data class SourceRating(
    @SerialName("Source") val source: String,
    @SerialName("Value") val value: String
)