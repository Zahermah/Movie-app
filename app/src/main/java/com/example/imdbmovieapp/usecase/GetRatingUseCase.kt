package com.example.imdbmovieapp.usecase

import com.example.imdbmovieapp.model.RatedMovie
import com.example.imdbmovieapp.repository.MovieRepository
import javax.inject.Inject

class GetRatingUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(imdbId: String): RatedMovie? {
        return movieRepository.getRating(imdbId)
    }
}