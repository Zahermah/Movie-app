package com.example.imdbmovieapp.usecase

import com.example.imdbmovieapp.model.MovieSummary
import com.example.imdbmovieapp.repository.MovieRepository
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(query: String): Result<List<MovieSummary>> {
        val sanitizedQuery = query
            .replace(Regex("[^a-zA-Z0-9\\s]"), "") // Remove special characters
            .trim()

        if (sanitizedQuery.isBlank()) return Result.success(emptyList())
        if (sanitizedQuery.length < 2) return Result.success(emptyList())

        return movieRepository.searchMovies(sanitizedQuery)
            .map { movies -> movies.distinctBy { it.imdbId } }
    }
}