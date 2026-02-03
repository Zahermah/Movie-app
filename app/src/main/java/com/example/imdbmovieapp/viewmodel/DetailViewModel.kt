package com.example.imdbmovieapp.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imdbmovieapp.model.MovieDetail
import com.example.imdbmovieapp.model.RatedMovie
import com.example.imdbmovieapp.usecase.DeleteRatingUseCase
import com.example.imdbmovieapp.usecase.GetMovieDetailUseCase
import com.example.imdbmovieapp.usecase.GetRatingUseCase
import com.example.imdbmovieapp.usecase.SaveRatingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getRatingUseCase: GetRatingUseCase,
    private val saveRatingUseCase: SaveRatingUseCase,
    private val deleteRatingUseCase: DeleteRatingUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val imdbId: String = checkNotNull(savedStateHandle["imdbId"])

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private val _userRating = MutableStateFlow<Int?>(null)
    val userRating: StateFlow<Int?> = _userRating.asStateFlow()

    init {
        loadMovieDetail()
        loadUserRating()
    }

    private fun loadMovieDetail() {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading

            getMovieDetailUseCase(imdbId)
                .onSuccess { movie ->
                    _uiState.value = DetailUiState.Success(movie)
                }
                .onFailure { error ->
                    _uiState.value = DetailUiState.Error(
                        error.message ?: "Something went wrong"
                    )
                }
        }
    }

    private fun loadUserRating() {
        viewModelScope.launch {
            val rating = getRatingUseCase(imdbId)
            _userRating.value = rating?.userRating
        }
    }

    fun onRatingChanged(rating: Int?) {
        val currentState = _uiState.value
        if (currentState !is DetailUiState.Success) return

        viewModelScope.launch {
            if (rating == null) {
                deleteRatingUseCase(imdbId)
                _userRating.value = null
            } else {
                val movie = currentState.movie
                val ratedMovie = RatedMovie(
                    imdbId = movie.imdbId,
                    title = movie.title,
                    year = movie.year,
                    poster = movie.poster,
                    userRating = rating
                )
                saveRatingUseCase(ratedMovie)
                _userRating.value = rating
            }
        }
    }

    fun onRetry() {
        loadMovieDetail()
    }
}

sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Success(val movie: MovieDetail) : DetailUiState
    data class Error(val message: String) : DetailUiState
}