package movie.metropolis.app.screen.listing

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.theme.Theme
import kotlin.random.Random.Default.nextBoolean
import kotlin.random.Random.Default.nextBytes

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MoviesScreen(
    padding: PaddingValues,
    onClickMovie: (String, upcoming: Boolean) -> Unit,
    state: LazyListState,
    stateAvailable: PagerState,
    stateUpcoming: LazyListState,
    onPermissionsRequested: suspend (Array<String>) -> Boolean,
    viewModel: ListingViewModel = hiltViewModel()
) {
    val current by viewModel.current.collectAsState()
    val upcoming by viewModel.upcoming.collectAsState()
    val scope = rememberCoroutineScope()
    MoviesScreen(
        current = current,
        upcoming = upcoming,
        onClickFavorite = {
            scope.launch {
                val granted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    onPermissionsRequested(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
                } else {
                    true
                }
                if (!granted) return@launch
                viewModel.toggleFavorite(it)
            }
        },
        onClick = onClickMovie,
        padding = padding,
        state = state,
        stateAvailable = stateAvailable,
        stateUpcoming = stateUpcoming
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun MoviesScreen(
    current: Loadable<List<MovieView>>,
    upcoming: Loadable<List<MovieView>>,
    onClickFavorite: (MovieView) -> Unit,
    onClick: (String, upcoming: Boolean) -> Unit,
    padding: PaddingValues = PaddingValues(0.dp),
    state: LazyListState = rememberLazyListState(),
    stateAvailable: PagerState = rememberPagerState(),
    stateUpcoming: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = state,
        contentPadding = padding
    ) {
        item("available-headline") {
            Text(
                text = "Now Available",
                modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp),
                style = MaterialTheme.typography.titleLarge
            )
        }
        item("available-pager") {
            MoviePager(
                items = current,
                onClick = { onClick(it, false) },
                modifier = Modifier.fillMaxWidth(),
                state = stateAvailable
            )
        }
        item("upcoming-headline") {
            Text(
                text = "Upcoming",
                modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                style = MaterialTheme.typography.titleLarge
            )
        }
        item("upcoming-row") {
            MovieRow(
                items = upcoming,
                isShowing = false,
                onClickFavorite = onClickFavorite,
                onClick = { onClick(it, true) },
                state = stateUpcoming
            )
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
private fun Preview(@PreviewParameter(ListMovieViewProvider::class) movies: List<MovieView>) {
    Theme {
        MoviesScreen(
            current = Loadable.loading(),
            upcoming = Loadable.success(movies),
            onClickFavorite = {},
            onClick = { _, _ -> }
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
        override val video: VideoView? = PreviewVideo(),
        override val favorite: Boolean = nextBoolean()
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