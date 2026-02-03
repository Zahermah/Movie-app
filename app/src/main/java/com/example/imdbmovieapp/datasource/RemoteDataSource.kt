package com.example.imdbmovieapp.datasource

import com.example.imdbmovieapp.repository.interfaces.IRemoteDataSource
import com.example.imdbmovieapp.model.MovieDetail
import com.example.imdbmovieapp.model.MovieSummary
import com.example.imdbmovieapp.module.IoDispatcher
import com.example.imdbmovieapp.api.OmdbApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: OmdbApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : IRemoteDataSource {

    override suspend fun searchMovies(query: String): Result<List<MovieSummary>> {
        return withContext(ioDispatcher) {
            try {
                val response = api.searchMovies(query)
                if (response.response == "True") {
                    Result.success(value = response.search ?: emptyList())
                } else {
                    Result.failure(Exception(response.error ?: "Unknown error"))
                }
            } catch (e: Exception) {
                Result.failure(exception = e)
            }
        }
    }

    override suspend fun getMovieDetail(imdbId: String): Result<MovieDetail> {
        return withContext(ioDispatcher) {
            try {
                val response = api.getMovieDetail(imdbId)
                Result.success(value = response)
            } catch (e: Exception) {
                Result.failure(exception = e)
            }
        }
    }
}