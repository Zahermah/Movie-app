package com.example.imdbmovieapp.usecase

import com.example.imdbmovieapp.model.RatedMovie
import com.example.imdbmovieapp.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRatedMoviesUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    operator fun invoke(): Flow<List<RatedMovie>>{
        return movieRepository.getRatedMovies()
    }
}