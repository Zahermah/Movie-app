package com.example.imdbmovieapp.repository.interfaces

import com.example.imdbmovieapp.model.MovieDetail
import com.example.imdbmovieapp.model.MovieSummary

interface IRemoteDataSource {
    suspend fun searchMovies(query: String): Result<List<MovieSummary>>
    suspend fun getMovieDetail(imdbId:String): Result<MovieDetail>
}