package com.example.imdbmovieapp.usecase

import com.example.imdbmovieapp.repository.MovieRepository
import javax.inject.Inject

class DeleteRatingUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(imdbId: String) = movieRepository.deleteRating(imdbId)
}