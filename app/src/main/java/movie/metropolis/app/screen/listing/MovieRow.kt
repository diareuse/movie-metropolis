package movie.metropolis.app.screen.listing

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.onEmpty
import movie.metropolis.app.screen.onFailure
import movie.metropolis.app.screen.onLoading
import movie.metropolis.app.screen.onSuccess
import movie.style.theme.Theme

@Composable
fun MovieRow(
    items: Loadable<List<MovieView>>,
    isShowing: Boolean,
    onClickFavorite: (MovieView) -> Unit,
    onClick: (String) -> Unit,
    state: LazyListState = rememberLazyListState()
) {
    items.onSuccess {
        MovieRow(
            items = items.getOrNull().orEmpty(),
            isShowing = isShowing,
            onClick = onClick,
            onClickFavorite = onClickFavorite,
            state = state
        )
    }.onLoading {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
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
            modifier = Modifier
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
            modifier = Modifier
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
    items: List<MovieView>,
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
            MovieItem(
                name = item.name,
                subtext = if (isShowing) item.releasedAt else item.availableFrom,
                poster = item.poster,
                isFavorite = item.favorite,
                onClick = { onClick(item.id) },
                onClickFavorite = { onClickFavorite(item) },
                onLongPress = { showPopup = it }
            )
            MoviePopup(
                isVisible = showPopup,
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
                items = Loadable.success(movies),
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
                items = Loadable.success(emptyList()),
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