package movie.metropolis.app.screen.listing

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
    viewModel: ListingViewModel = hiltViewModel()
) {
    val current by viewModel.current.collectAsState()
    val upcoming by viewModel.upcoming.collectAsState()
    MoviesScreen(
        current = current,
        upcoming = upcoming
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MoviesScreen(
    current: Loadable<List<MovieView>>,
    upcoming: Loadable<List<MovieView>>,
    onClickVideo: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text("Movies")
            })
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            item {
                Text(
                    text = "Upcoming Movies",
                    modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            item {
                MoviePager(
                    items = upcoming,
                    isShowing = false,
                    onClickVideo = onClickVideo
                )
            }
            item {
                Text(
                    text = "Showing right now",
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            item {
                MoviePager(
                    items = current,
                    isShowing = true,
                    onClickVideo = onClickVideo
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview(@PreviewParameter(ListMovieViewProvider::class) movies: List<MovieView>) {
    Theme {
        MoviesScreen(
            Loadable.loading(),
            Loadable.success(movies)
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
        override val aspectRatio: Float = 3f / 5,
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