package com.example.imdbmovieapp.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.imdbmovieapp.model.RatedMovie
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RatingsStorage @Inject constructor(
    @ApplicationContext val context: Context
) {
    private val Context.dataStore by preferencesDataStore(name = "ratings")
    private val ratingsKey = stringPreferencesKey(name = "rated_movies")
    private val json = Json { ignoreUnknownKeys = true }

    val ratedMovies: Flow<List<RatedMovie>> = context.dataStore.data
        .map { prefs ->
            val jsonString = prefs[ratingsKey] ?: "[]"
            json.decodeFromString<List<RatedMovie>>(jsonString)
        }

    suspend fun saveRating(movie: RatedMovie) {
        context.dataStore.edit { prefs ->
            val jsonString = prefs[ratingsKey] ?: "[]"
            val current = json.decodeFromString<List<RatedMovie>>(jsonString).toMutableList()

            current.removeAll { it.imdbId == movie.imdbId }
            // add a rating so the new object is top of the list   current.add(0,movie)
            current.add(movie)

            prefs[ratingsKey] = json.encodeToString(value = current)
        }
    }

    suspend fun getRating(imdbId: String): RatedMovie? {
        return ratedMovies.first().find { it.imdbId == imdbId }
    }

    suspend fun deleteRating(imdbId: String) {
        context.dataStore.edit { prefs ->
            val jsonString = prefs[ratingsKey] ?: "[]"
            val current = json.decodeFromString<List<RatedMovie>>(jsonString).toMutableList()

            current.removeAll { it.imdbId == imdbId }

            prefs[ratingsKey] = json.encodeToString(value = current)
        }
    }
}