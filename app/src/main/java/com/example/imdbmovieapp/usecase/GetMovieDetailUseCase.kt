package com.example.imdbmovieapp.usecase

import com.example.imdbmovieapp.model.MovieDetail
import com.example.imdbmovieapp.repository.MovieRepository
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(imdbId:String): Result<MovieDetail>{
        return movieRepository.getMovieDetail(imdbId)
    }
}