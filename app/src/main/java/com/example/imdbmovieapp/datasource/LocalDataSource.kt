package com.example.imdbmovieapp.datasource

import com.example.imdbmovieapp.repository.interfaces.ILocalDataSource
import com.example.imdbmovieapp.datastore.RatingsStorage
import com.example.imdbmovieapp.model.RatedMovie
import com.example.imdbmovieapp.module.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val storage: RatingsStorage,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ILocalDataSource {

    override fun getRatedMovies(): Flow<List<RatedMovie>> {
        return storage.ratedMovies.flowOn(ioDispatcher)
    }

    override suspend fun getRating(imdbId: String): RatedMovie? {
        return withContext(ioDispatcher) {
            storage.getRating(imdbId)
        }
    }

    override suspend fun saveRating(movie: RatedMovie) {
        withContext(ioDispatcher) {
            storage.saveRating(movie)
        }
    }

    override suspend fun deleteRating(imdbId: String) {
        withContext(ioDispatcher) {
            storage.deleteRating(imdbId)
        }
    }
}