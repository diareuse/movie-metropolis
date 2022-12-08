package movie.metropolis.app.screen.listing

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.theme.Theme
import kotlin.random.Random.Default.nextBytes

@Composable
fun MoviesScreen(
    padding: PaddingValues,
    onClickVideo: (String) -> Unit,
    onClickMovie: (String) -> Unit,
    state: LazyListState,
    stateAvailable: LazyListState,
    stateUpcoming: LazyListState,
    viewModel: ListingViewModel = hiltViewModel()
) {
    val current by viewModel.current.collectAsState()
    val upcoming by viewModel.upcoming.collectAsState()
    MoviesScreen(
        current = current,
        upcoming = upcoming,
        onClickVideo = onClickVideo,
        onClick = onClickMovie,
        padding = padding,
        state = state,
        stateAvailable = stateAvailable,
        stateUpcoming = stateUpcoming
    )
}

@Composable
private fun MoviesScreen(
    current: Loadable<List<MovieView>>,
    upcoming: Loadable<List<MovieView>>,
    onClickVideo: (String) -> Unit,
    onClick: (String) -> Unit,
    padding: PaddingValues = PaddingValues(0.dp),
    state: LazyListState = rememberLazyListState(),
    stateAvailable: LazyListState = rememberLazyListState(),
    stateUpcoming: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = state,
        contentPadding = padding
    ) {
        item {
            Text(
                text = "Now Available",
                modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp),
                style = MaterialTheme.typography.titleLarge
            )
        }
        item {
            MovieRow(
                items = current,
                isShowing = true,
                onClickVideo = onClickVideo,
                onClick = onClick,
                state = stateAvailable
            )
        }
        item {
            Text(
                text = "Upcoming",
                modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                style = MaterialTheme.typography.titleLarge
            )
        }
        item {
            MovieRow(
                items = upcoming,
                isShowing = false,
                onClickVideo = onClickVideo,
                onClick = onClick,
                state = stateUpcoming
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview(@PreviewParameter(ListMovieViewProvider::class) movies: List<MovieView>) {
    Theme {
        MoviesScreen(
            current = Loadable.loading(),
            upcoming = Loadable.success(movies),
            onClickVideo = {},
            onClick = {}
        )
    }
}

class ListMovieViewProvider : CollectionPreviewParameterProvider<List<MovieView>>(buildList {
    val provider = MovieViewProvider()
    this += provider.values.toList()
})

class MovieViewProvider :
    CollectionPreviewParameterProvider<MovieView>(List(10) { PreviewMovie() }) {
    private data class PreviewMovie(
        override val id: String = String(nextBytes(10)),
        override val name: String = listOf(
            "Black Adam",
            "Black Panther: Wakanda Forever",
            "Strange World",
            "The Fabelmans"
        ).random(),
        override val releasedAt: String = "2022",
        override val duration: String = "1h 35m",
        override val availableFrom: String = "21. 3. 2022",
        override val directors: List<String> = emptyList(),
        override val cast: List<String> = emptyList(),
        override val countryOfOrigin: String = "USA",
        override val poster: ImageView? = PreviewImage(),
        override val video: VideoView? = PreviewVideo()
    ) : MovieView

    private data class PreviewImage(
        override val aspectRatio: Float = DefaultPosterAspectRatio,
        override val url: String = listOf(
            "https://www.cinemacity.cz/xmedia-cw/repo/feats/posters/5145S2R-lg.jpg",
            "https://www.cinemacity.cz/xmedia-cw/repo/feats/posters/5219S2R-lg.jpg",
            "https://www.cinemacity.cz/xmedia-cw/repo/feats/posters/5228D2R-lg.jpg",
            "https://www.cinemacity.cz/xmedia-cw/repo/feats/posters/5392S2R-lg.jpg"
        ).random()
    ) : ImageView

    private data class PreviewVideo(
        override val url: String = ""
    ) : VideoView
}