package movie.metropolis.app.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.nestedscroll.*
import androidx.compose.ui.tooling.preview.*
import movie.style.layout.PreviewLayout
import movie.style.util.pc

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenScaffold(
    titleMovies: @Composable () -> Unit,
    titleCinemas: @Composable () -> Unit,
    profile: @Composable () -> Unit,
    movies: @Composable (PaddingValues) -> Unit,
    cinemas: @Composable (PaddingValues) -> Unit,
    modifier: Modifier = Modifier,
    initialPage: Int = 0,
    pagerState: PagerState = rememberPagerState(initialPage) { 2 },
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
) = Scaffold(
    modifier = modifier,
    topBar = {
        LargeTopAppBar(
            modifier = Modifier
                .windowInsetsPadding(
                    WindowInsets.systemBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
                )
                .padding(1.pc)
                .clip(MaterialTheme.shapes.medium),
            windowInsets = WindowInsets(0),
            title = {
                when (pagerState.currentPage) {
                    0 -> titleMovies()
                    1 -> titleCinemas()
                }
            },
            scrollBehavior = scrollBehavior,
            actions = {
                profile()
            }
        )
    }
) { padding ->
    HorizontalPager(pagerState) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
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