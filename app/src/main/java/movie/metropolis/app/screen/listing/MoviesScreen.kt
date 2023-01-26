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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import movie.metropolis.app.R
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.detail.plus
import movie.metropolis.app.screen.home.HomeScreenLayout
import movie.style.theme.Theme
import kotlin.random.Random.Default.nextBoolean
import kotlin.random.Random.Default.nextBytes

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
    padding: PaddingValues,
    onClickMovie: (String, upcoming: Boolean) -> Unit,
    state: LazyListState,
    stateAvailable: PagerState,
    stateUpcoming: LazyListState,
    onPermissionsRequested: suspend (Array<String>) -> Boolean,
    profileIcon: @Composable () -> Unit,
    viewModel: ListingViewModel = hiltViewModel()
) {
    val current by viewModel.current.collectAsState()
    val upcoming by viewModel.upcoming.collectAsState()
    val scope = rememberCoroutineScope()
    HomeScreenLayout(
        profileIcon = profileIcon,
        title = { Text(stringResource(R.string.movies)) }
    ) { innerPadding, behavior ->
        MoviesScreenContent(
            behavior = behavior,
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
            padding = padding + innerPadding,
            state = state,
            stateAvailable = stateAvailable,
            stateUpcoming = stateUpcoming
        )
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun MoviesScreenContent(
    current: Loadable<List<MovieView>>,
    upcoming: Loadable<List<MovieView>>,
    onClickFavorite: (MovieView) -> Unit,
    onClick: (String, upcoming: Boolean) -> Unit,
    behavior: TopAppBarScrollBehavior,
    padding: PaddingValues = PaddingValues(0.dp),
    state: LazyListState = rememberLazyListState(),
    stateAvailable: PagerState = rememberPagerState(),
    stateUpcoming: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = Modifier
            .nestedScroll(behavior.nestedScrollConnection)
            .fillMaxSize(),
        state = state,
        contentPadding = padding
    ) {
        item("available-headline") {
            Text(
                text = stringResource(R.string.now_available),
                modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp),
                style = Theme.textStyle.title
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
                text = stringResource(R.string.upcoming),
                modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                style = Theme.textStyle.title
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


@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun Preview(@PreviewParameter(ListMovieViewProvider::class) movies: List<MovieView>) {
    Theme {
        HomeScreenLayout(
            profileIcon = {},
            title = {}
        ) { padding, behavior ->
            MoviesScreenContent(
                padding = padding,
                current = Loadable.loading(),
                upcoming = Loadable.success(movies),
                onClickFavorite = {},
                onClick = { _, _ -> },
                behavior = behavior
            )
        }
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
        override val favorite: Boolean = nextBoolean(),
        override val rating: String? = "75%",
        override val posterLarge: ImageView? = PreviewImage()
    ) : MovieView

    private data class PreviewImage(
        override val aspectRatio: Float = DefaultPosterAspectRatio,
        override val url: String = listOf(
            "https://www.cinemacity.cz/xmedia-cw/repo/feats/posters/5145S2R-lg.jpg",
            "https://www.cinemacity.cz/xmedia-cw/repo/feats/posters/5219S2R-lg.jpg",
            "https://www.cinemacity.cz/xmedia-cw/repo/feats/posters/5228D2R-lg.jpg",
            "https://www.cinemacity.cz/xmedia-cw/repo/feats/posters/5392S2R-lg.jpg"
        ).random(),
        override val spotColor: Color = Color.LightGray
    ) : ImageView

    private data class PreviewVideo(
        override val url: String = ""
    ) : VideoView
}