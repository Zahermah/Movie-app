package com.example.imdbmovieapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.imdbmovieapp.R
import com.example.imdbmovieapp.ui.theme.StarYellow

@Composable
fun RatingBar(
    rating: Int,
    onRatingChanged: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (i in 1..10) {
            Icon(
                painter = if (i <= rating) painterResource(id = R.drawable.star) else painterResource(
                    id = R.drawable.star_small
                ),
                contentDescription = "Star $i",

                tint = if (i <= rating) StarYellow else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        if (i == rating) {
                            onRatingChanged(null)  // Deselect if same rating
                        } else {
                            onRatingChanged(i)
                        }
                    }
            )
        }
    }
}