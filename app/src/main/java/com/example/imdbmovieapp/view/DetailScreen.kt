package com.example.imdbmovieapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.imdbmovieapp.R
import com.example.imdbmovieapp.components.ErrorState
import com.example.imdbmovieapp.components.LoadingState
import com.example.imdbmovieapp.components.RatingBar
import com.example.imdbmovieapp.model.MovieDetail
import com.example.imdbmovieapp.model.SourceRating
import com.example.imdbmovieapp.viewmodel.DetailUiState
import com.example.imdbmovieapp.viewmodel.DetailViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    onBackClick: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userRating by viewModel.userRating.collectAsStateWithLifecycle()

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val movie = (uiState as? DetailUiState.Success)?.movie

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (uiState is DetailUiState.Success) {
                        Text(
                            text = movie?.type ?: "Details",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.navigate_back),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->

        when (val state = uiState) {
            is DetailUiState.Loading -> {
                LoadingState()
            }

            is DetailUiState.Error -> {
                ErrorState(
                    message = state.message,
                    onRetry = viewModel::onRetry
                )
            }

            is DetailUiState.Success -> {
                movie?.let {
                    MovieDetailContent(
                        movie = it,
                        userRating = userRating,
                        onRatingChanged = { rating ->
                            viewModel.onRatingChanged(rating)
                            scope.launch {
                                val message = if (rating != null) {
                                    "You rated ${it.title} $rating/10"
                                } else {
                                    "You removed your rating for ${it.title}"
                                }
                                snackBarHostState.showSnackbar(
                                    message = message,
                                    duration = SnackbarDuration.Short
                                )
                            }
                        },
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}

@Composable
private fun MovieDetailContent(
    movie: MovieDetail,
    userRating: Int?,
    onRatingChanged: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Header with poster and basic info
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AsyncImage(
                    model = movie.poster,
                    contentDescription = movie.title,
                    modifier = Modifier
                        .width(120.dp)
                        .height(180.dp)
                        .clip(shape = RoundedCornerShape(size = 8.dp)),
                    contentScale = ContentScale.Crop
                )

                Column {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${movie.year} • ${movie.runtime}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = movie.genre,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "Rated: ${movie.rated}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Ratings from sources
        item {
            RatingsFromSources(ratings = movie.ratings)
        }

        // Box Office & Awards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoCard(
                    title = "Box Office",
                    value = movie.boxOffice,
                    modifier = Modifier.weight(1f)
                )
                InfoCard(
                    title = "Awards",
                    value = movie.awards,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Director
        item {
            Text(
                text = "Director",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = movie.director,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Actors
        item {
            Text(
                text = "Actors",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = movie.actors,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Plot
        item {
            Text(
                text = "Plot",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = movie.plot,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // User rating
        item {
            RatingSection(
                userRating = userRating,
                onRatingChanged = onRatingChanged
            )
        }
    }
}

@Composable
private fun RatingsFromSources(ratings: List<SourceRating>) {
    if (ratings.isEmpty()) return

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Ratings",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ratings.forEach { rating ->
                    RatingItem(rating = rating)
                }
            }
        }
    }
}

@Composable
private fun RatingItem(rating: SourceRating) {

    val icon = when {
        rating.source.contains("Internet Movie Database", ignoreCase = true) -> R.drawable.imdb_icon
        rating.source.contains("Rotten", ignoreCase = true) -> R.drawable.tomato_icon
        rating.source.contains("Metacritic", ignoreCase = true) -> R.drawable.metacritic_icon
        else -> R.drawable.ic_placholder
    }

    val shortName = when {
        rating.source.contains("Internet Movie Database", ignoreCase = true) -> "IMDB"
        rating.source.contains("Rotten", ignoreCase = true) -> "Rotten T."
        rating.source.contains("Metacritic", ignoreCase = true) -> "Metacritic"
        else -> rating.source
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = shortName,
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = rating.value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = shortName,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun InfoCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(all = 12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = if (value == "N/A") "—" else value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun RatingSection(
    userRating: Int?,
    onRatingChanged: (Int?) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(all = 16.dp)
        ) {
            Text(
                text = "Your Rating",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            RatingBar(
                rating = userRating ?: 0,
                onRatingChanged = onRatingChanged
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (userRating != null) "$userRating/10" else "Tap to rate",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}