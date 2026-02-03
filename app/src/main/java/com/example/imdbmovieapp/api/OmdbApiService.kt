package com.example.imdbmovieapp.api

import com.example.imdbmovieapp.BuildConfig
import com.example.imdbmovieapp.model.MovieDetail
import com.example.imdbmovieapp.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApiService {

    @GET("/")
    suspend fun searchMovies(
        @Query("s") query: String,
        @Query("page") page: Int = 1,
        @Query("apikey") apiKey: String = BuildConfig.OMDB_API_KEY
    ): SearchResponse

    @GET("/")
    suspend fun getMovieDetail(
        @Query("i") imdbId: String,
        @Query("apikey") apiKey: String = BuildConfig.OMDB_API_KEY
    ): MovieDetail
}