package movie.metropolis.app.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.screen.movie.component.MovieViewProvider
import movie.metropolis.app.ui.home.component.MovieBox
import movie.style.Image
import movie.style.layout.DefaultPosterAspectRatio
import movie.style.layout.PreviewLayout
import movie.style.rememberImageState
import movie.style.util.pc

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeScreenState,
    onMovieClick: (id: String, upcoming: Boolean) -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) = HomeScreenScaffold(
    modifier = modifier,
    titleMovies = { Text("Movies") },
    titleCinemas = { Text("Cinemas") },
    profile = {
        IconButton(onProfileClick) {
            Icon(Icons.Default.AccountCircle, null)
        }
    },
    movies = { padding ->
        LazyVerticalStaggeredGrid(
            modifier = Modifier.padding(horizontal = 1.pc),
            columns = StaggeredGridCells.Adaptive(150.dp),
            contentPadding = padding,
            horizontalArrangement = Arrangement.spacedBy(1.pc),
            verticalItemSpacing = 1.pc
        ) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Text("Currently showing", style = MaterialTheme.typography.titleMedium)
            }
            items(state.current) {
                MovieBox(
                    onClick = { onMovieClick(it.id, false) },
                    aspectRatio = it.poster?.aspectRatio ?: DefaultPosterAspectRatio,
                    name = { Text(it.name) },
                    poster = { Image(rememberImageState(it.poster?.url)) },
                    rating = {
                        val r = it.rating
                        if (r != null) Text(r)
                    },
                    category = {}
                )
            }
            item(span = StaggeredGridItemSpan.FullLine) {
                Text("Upcoming", style = MaterialTheme.typography.titleMedium)
            }
            items(state.upcoming) {
                MovieBox(
                    onClick = { onMovieClick(it.id, true) },
                    aspectRatio = it.poster?.aspectRatio ?: DefaultPosterAspectRatio,
                    name = { Text(it.name) },
                    poster = { Image(rememberImageState(it.poster?.url)) },
                    rating = {
                        val r = it.rating
                        if (r != null) Text(r)
                    },
                    category = {}
                )
            }
        }
    },
    cinemas = {}
)

@PreviewLightDark
@PreviewFontScale
@Composable
private fun HomeScreenPreview() = PreviewLayout {
    val state = remember { HomeScreenState() }
    LaunchedEffect(Unit) {
        state.current.addAll(MovieViewProvider().values)
    }
    HomeScreen(
        state = state,
        onMovieClick = { _, _ -> },
        onProfileClick = {}
    )
}