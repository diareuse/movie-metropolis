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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.theme.Theme

@Composable
fun MovieRow(
    items: Loadable<List<MovieView>>,
    isShowing: Boolean,
    onClickVideo: (String) -> Unit,
    onClick: (String) -> Unit,
    state: LazyListState = rememberLazyListState()
) {
    when {
        items.isLoading -> Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .horizontalScroll(rememberScrollState(), enabled = false)
                .padding(24.dp)
        ) {
            MovieItem()
            MovieItem()
            MovieItem()
        }

        items.isSuccess -> MovieRow(
            items = items.getOrNull().orEmpty(),
            isShowing = isShowing,
            onClick = onClick,
            onClickVideo = onClickVideo,
            state = state
        )
    }
}

@Composable
private fun MovieRow(
    items: List<MovieView>,
    isShowing: Boolean,
    onClickVideo: (String) -> Unit,
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
            MovieItem(
                name = item.name,
                subtext = if (isShowing) item.releasedAt else item.availableFrom,
                video = item.video,
                poster = item.poster,
                onClickVideo = onClickVideo,
                onClick = { onClick(item.id) }
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
                onClickVideo = {},
                onClick = {}
            )
            MovieRow(
                items = Loadable.success(movies),
                isShowing = false,
                onClickVideo = {},
                onClick = {}
            )
        }
    }
}