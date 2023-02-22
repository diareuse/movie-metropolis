package movie.metropolis.app.screen.listing.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.onEmpty
import movie.metropolis.app.presentation.onFailure
import movie.metropolis.app.presentation.onLoading
import movie.metropolis.app.presentation.onSuccess
import movie.metropolis.app.screen.listing.ListMovieViewProvider
import movie.style.state.ImmutableList
import movie.style.state.ImmutableList.Companion.immutable
import movie.style.theme.Theme

@Composable
fun MovieRow(
    items: Loadable<ImmutableList<MovieView>>,
    isShowing: Boolean,
    onClickFavorite: (MovieView) -> Unit,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState()
) {
    items.onSuccess {
        MovieRow(
            modifier = modifier,
            items = it,
            isShowing = isShowing,
            onClick = onClick,
            onClickFavorite = onClickFavorite,
            state = state
        )
    }.onLoading {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .horizontalScroll(rememberScrollState(), enabled = false)
                .padding(24.dp)
        ) {
            MovieItem()
            MovieItem()
            MovieItem()
        }
    }.onEmpty {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .horizontalScroll(rememberScrollState(), enabled = false)
                .padding(24.dp)
        ) {
            MovieItemEmpty()
            MovieItemEmpty()
            MovieItemEmpty()
        }
    }.onFailure {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .horizontalScroll(rememberScrollState(), enabled = false)
                .padding(24.dp)
        ) {
            MovieItemError()
            MovieItemError()
            MovieItemError()
        }
    }
}

@Composable
private fun MovieRow(
    items: ImmutableList<MovieView>,
    isShowing: Boolean,
    onClickFavorite: (MovieView) -> Unit,
    onClick: (String) -> Unit,
    state: LazyListState,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(24.dp),
        state = state
    ) {
        items(items, key = MovieView::id) { item ->
            var showPopup by remember { mutableStateOf(false) }
            if (isShowing)
                MovieItem(
                    rating = item.rating,
                    poster = item.poster,
                    onClick = { onClick(item.id) },
                    onLongPress = { showPopup = it }
                )
            else
                MovieItem(
                    subtext = item.availableFrom,
                    poster = item.poster,
                    isFavorite = item.favorite,
                    onClick = { onClick(item.id) },
                    onClickFavorite = { onClickFavorite(item) },
                    onLongPress = { showPopup = it }
                )
            MoviePopup(
                isVisible = showPopup,
                onVisibilityChanged = { showPopup = false },
                url = item.posterLarge?.url ?: "",
                year = item.releasedAt,
                director = item.directors.joinToString(),
                name = item.name,
                aspectRatio = item.posterLarge?.aspectRatio ?: DefaultPosterAspectRatio
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview(
    @PreviewParameter(ListMovieViewProvider::class, 1)
    movies: List<MovieView>
) {
    Theme {
        Column {
            MovieRow(
                items = Loadable.loading(),
                isShowing = false,
                onClickFavorite = {},
                onClick = {}
            )
            MovieRow(
                items = Loadable.success(movies.immutable()),
                isShowing = false,
                onClickFavorite = {},
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview2() {
    Theme {
        Column {
            MovieRow(
                items = Loadable.success(emptyList<MovieView>().immutable()),
                isShowing = false,
                onClickFavorite = {},
                onClick = {}
            )
            MovieRow(
                items = Loadable.failure(Throwable()),
                isShowing = false,
                onClickFavorite = {},
                onClick = {}
            )
        }
    }
}