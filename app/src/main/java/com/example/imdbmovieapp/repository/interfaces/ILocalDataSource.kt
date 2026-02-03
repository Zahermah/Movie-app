package com.example.imdbmovieapp.repository.interfaces

import com.example.imdbmovieapp.model.RatedMovie
import kotlinx.coroutines.flow.Flow

interface ILocalDataSource {
    fun getRatedMovies(): Flow<List<RatedMovie>>
    suspend fun getRating(imdbId: String): RatedMovie?
    suspend fun saveRating(movie: RatedMovie)
    suspend fun deleteRating(imdbId: String)
}