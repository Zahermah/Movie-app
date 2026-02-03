package com.example.imdbmovieapp.usecase

import com.example.imdbmovieapp.model.RatedMovie
import com.example.imdbmovieapp.repository.MovieRepository
import javax.inject.Inject

class SaveRatingUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movie: RatedMovie) = movieRepository.saveRating(movie)
}