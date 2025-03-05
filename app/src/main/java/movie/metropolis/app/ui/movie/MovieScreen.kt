@file:OptIn(ExperimentalSharedTransitionApi::class)

package movie.metropolis.app.ui.movie

import androidx.compose.animation.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import movie.metropolis.app.R
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.screen.movie.component.MovieDetailViewProvider
import movie.metropolis.app.ui.movie.component.ActorColumn
import movie.metropolis.app.ui.movie.component.RatingText
import movie.style.Image
import movie.style.layout.PreviewLayout
import movie.style.rememberImageState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.MovieScreen(
    animationScope: AnimatedContentScope,
    showPurchase: Boolean,
    detail: MovieDetailView,
    onBackClick: () -> Unit,
    onBuyClick: () -> Unit,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) = MovieScreenScaffold(
    modifier = modifier.sharedBounds(
        rememberSharedContentState("movie-${detail.id}"),
        animationScope
    ),
    title = { Text(detail.name) },
    navigationIcon = {
        IconButton(onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
        }
    },
    backdrop = { Image(rememberImageState(detail.backdrop?.url)) },
    poster = {
        Image(
            modifier = Modifier,
            state = rememberImageState(detail.poster?.url)
        )
    },
    name = { Text(detail.nameOriginal) },
    duration = { Text(detail.duration) },
    releasedAt = { Text(detail.releasedAt) },
    availableFrom = { Text(detail.availableFrom) },
    country = { Text(detail.countryOfOrigin) },
    cast = {
        for (cast in detail.cast) {
            ActorColumn(
                icon = {
                    Image(
                        state = rememberImageState(cast.image),
                        placeholderError = painterResource(R.drawable.ic_person)
                    )
                },
                name = { Text(cast.name, minLines = 2) }
            )
        }
    },
    directors = {
        for (director in detail.directors) {
            ActorColumn(
                icon = {
                    Image(
                        state = rememberImageState(director.image),
                        placeholderError = painterResource(R.drawable.ic_person)
                    )
                },
                name = { Text(director.name, minLines = 2) }
            )
        }
    },
    description = { Text(detail.description) },
    trailer = {
        val t = detail.trailer
        if (t != null) IconButton({ onLinkClick(t.url) }) {
            Icon(Icons.Default.PlayArrow, null)
        }
    },
    link = {
        val u = detail.url
        if (u.isNotBlank()) IconButton({ onLinkClick(u) }) {
            Icon(Icons.Default.Info, null)
        }
    },
    rating = {
        val r = detail.rating
        if (r != null) RatingText(r, detail.ratingNumber)
    },
    purchase = {
        if (showPurchase) IconButton(onBuyClick) {
            Icon(Icons.Default.ShoppingCart, null)
        }
    }
)

@PreviewLightDark
@PreviewFontScale
@Composable
private fun MovieScreenPreview() = PreviewLayout {
    SharedTransitionLayout {
        AnimatedContent(true) {
            MovieScreen(
                animationScope = this,
                showPurchase = it,
                detail = MovieDetailViewProvider().values.first(),
                onBackClick = {},
                onBuyClick = {},
                onLinkClick = {})
        }
    }
}