package movie.metropolis.app.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import movie.style.layout.PreviewLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenScaffold(
    titleMovies: @Composable () -> Unit,
    titleCinemas: @Composable () -> Unit,
    profile: @Composable () -> Unit,
    tickets: @Composable () -> Unit,
    movies: @Composable (PaddingValues) -> Unit,
    cinemas: @Composable (PaddingValues) -> Unit,
    modifier: Modifier = Modifier,
    initialPage: Int = 0,
    pagerState: PagerState = rememberPagerState(initialPage) { 2 }
) = Scaffold(
    modifier = modifier,
    topBar = {
        Spacer(
            modifier = Modifier
                .windowInsetsPadding(
                    WindowInsets.systemBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
                )
        )
    }
) { padding ->
    HorizontalPager(pagerState, userScrollEnabled = false) {
        Box(
            modifier = Modifier.fillMaxSize(),
            propagateMinConstraints = true
        ) {
            when (it) {
                0 -> movies(padding)
                1 -> cinemas(padding)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@PreviewFontScale
@Composable
private fun HomeScreenScaffoldPreview() = PreviewLayout {
    HomeScreenScaffold(
        titleMovies = { Text("Movies") },
        titleCinemas = { Text("Cinemas") },
        profile = {
            IconButton({}) {
                Icon(Icons.Default.AccountCircle, null)
            }
        },
        tickets = {
            IconButton({}) {
                Icon(Icons.Default.Email, null)
            }
        },
        movies = {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Green)
            )
        },
        cinemas = {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Yellow)
            )
        }
    )
}