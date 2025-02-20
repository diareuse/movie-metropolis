@file:OptIn(ExperimentalSharedTransitionApi::class)

package movie.metropolis.app.ui.movie

import androidx.compose.animation.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.screen.movie.component.MovieDetailViewProvider
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
    modifier = modifier,
    title = { Text(detail.name) },
    navigationIcon = {
        IconButton(onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
        }
    },
    backdrop = { Image(rememberImageState(detail.backdrop?.url)) },
    poster = {
        Image(
            modifier = Modifier.sharedElement(
                state = rememberSharedContentState("movie-${detail.id}"),
                animatedVisibilityScope = animationScope,
                placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize
            ),
            state = rememberImageState(detail.poster?.url)
        )
    },
    name = {
        Text(
            text = detail.nameOriginal,
            modifier = Modifier.sharedElement(
                state = rememberSharedContentState("name-${detail.id}"),
                animatedVisibilityScope = animationScope,
                placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize
            )
        )
    },
    duration = { Text(detail.duration) },
    releasedAt = { Text(detail.releasedAt) },
    availableFrom = { Text(detail.availableFrom) },
    country = { Text(detail.countryOfOrigin) },
    cast = {
        for (cast in detail.cast) {
            Text(cast.name)
        }
    },
    directors = {
        for (director in detail.directors) {
            Text(director.name)
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
        if (r != null) Text(r)
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