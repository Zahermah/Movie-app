package com.example.imdbmovieapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.imdbmovieapp.R
import com.example.imdbmovieapp.components.ConnectivityBanner
import com.example.imdbmovieapp.components.EmptyState
import com.example.imdbmovieapp.components.ErrorState
import com.example.imdbmovieapp.components.LoadingState
import com.example.imdbmovieapp.components.MovieCard
import com.example.imdbmovieapp.components.RatedMovieCard
import com.example.imdbmovieapp.model.MovieSummary
import com.example.imdbmovieapp.model.RatedMovie
import com.example.imdbmovieapp.viewmodel.SearchUiState
import com.example.imdbmovieapp.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    onMovieClick: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()
    val ratedMovies by viewModel.ratedMovies.collectAsStateWithLifecycle()
    val isConnected by viewModel.isConnected.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 24.dp)
    ) {
        ConnectivityBanner(isConnected = isConnected)
        SearchBar(
            query = searchQuery,
            onQueryChange = viewModel::onSearchQueryChanged,
            onSearch = viewModel::onSearch,
            onClear = viewModel::onClearSearch,
            enabled = isConnected
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = searchState) {
            is SearchUiState.Idle -> {
                RatedMoviesList(
                    ratedMovies = ratedMovies,
                    onMovieClick = onMovieClick
                )
            }

            is SearchUiState.Loading -> {
                LoadingState()
            }

            is SearchUiState.Empty -> {
                EmptyState(message = "No results found for '$searchQuery'")
            }

            is SearchUiState.Success -> {
                SearchResultsList(
                    movies = state.movies,
                    ratedMovies = ratedMovies,
                    onMovieClick = onMovieClick
                )
            }

            is SearchUiState.Error -> {
                ErrorState(
                    message = state.message,
                    onRetry = viewModel::onSearch
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = query,
        onValueChange = { newValue ->
            val filtered = newValue.filter { it.isLetterOrDigit() || it.isWhitespace() }
            onQueryChange(filtered)
        },
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { it.isFocused },
        placeholder = {
            Text(if (enabled) "Search movies..." else "Search unavailable offline")
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.search_icon),
                contentDescription = "Search"
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(
                        painter = painterResource(id = R.drawable.close),
                        contentDescription = "Clear"
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        singleLine = true
    )
}

@Composable
private fun RatedMoviesList(
    ratedMovies: List<RatedMovie>,
    onMovieClick: (String) -> Unit
) {
    if (ratedMovies.isEmpty()) {
        EmptyState(message = "You haven't rated anything yet.\nSearch for movies to get started!")
        return
    }

    Column {
        Text(
            text = "My Ratings",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(ratedMovies, key = { it.imdbId }) { movie ->
                RatedMovieCard(
                    movie = movie,
                    onClick = { onMovieClick(movie.imdbId) }
                )
            }
        }
    }
}

@Composable
private fun SearchResultsList(
    movies: List<MovieSummary>,
    ratedMovies: List<RatedMovie>,
    onMovieClick: (String) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(movies, key = { it.imdbId }) { movie ->
            val userRating = ratedMovies.find { it.imdbId == movie.imdbId }?.userRating
            MovieCard(
                movie = movie,
                userRating = userRating,
                onClick = { onMovieClick(movie.imdbId) }
            )
        }
    }
}