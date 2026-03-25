package com.example.imdbmovieapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdbmovieapp.repository.interfaces.INetworkObserver
import com.example.imdbmovieapp.model.MovieSummary
import com.example.imdbmovieapp.model.RatedMovie
import com.example.imdbmovieapp.usecase.GetRatedMoviesUseCase
import com.example.imdbmovieapp.usecase.SearchMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase,
    getRatedMoviesUseCase: GetRatedMoviesUseCase,
    networkObserver: INetworkObserver
) : ViewModel() {

    private val _searchQuery = MutableStateFlow(value = "")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val searchState: StateFlow<SearchUiState> = _searchState.asStateFlow()

    val isConnected: StateFlow<Boolean> = networkObserver.isConnected
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = true
        )

    val ratedMovies: StateFlow<List<RatedMovie>> = getRatedMoviesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = emptyList()
        )

    val ratingsMap: StateFlow<Map<String, Int>> = getRatedMoviesUseCase()
        .map { list -> list.associateBy({ it.imdbId }, { it.userRating }) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = emptyMap()
        )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onSearch() {
        val query = _searchQuery.value
        if (query.isBlank()) {
            _searchState.value = SearchUiState.Idle
            return
        }

        viewModelScope.launch {
            _searchState.value = SearchUiState.Loading

            searchMoviesUseCase(query)
                .onSuccess { movies ->
                    _searchState.value = if (movies.isEmpty()) {
                        SearchUiState.Empty
                    } else {
                        SearchUiState.Success(movies)
                    }
                }
                .onFailure { error ->
                    _searchState.value = SearchUiState.Error(
                        error.message ?: "Something went wrong"
                    )
                }
        }
    }

    fun onClearSearch() {
        _searchQuery.value = ""
        _searchState.value = SearchUiState.Idle
    }
}

sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object Loading : SearchUiState
    data object Empty : SearchUiState
    data class Success(val movies: List<MovieSummary>) : SearchUiState
    data class Error(val message: String) : SearchUiState
}