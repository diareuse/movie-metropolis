package movie.metropolis.app.ui.movie

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.nestedscroll.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import movie.style.layout.DefaultPosterAspectRatio
import movie.style.layout.PreviewLayout
import movie.style.util.pc

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MovieScreenScaffold(
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit,
    backdrop: @Composable () -> Unit,
    poster: @Composable () -> Unit,
    name: @Composable () -> Unit,
    duration: @Composable () -> Unit,
    releasedAt: @Composable () -> Unit,
    availableFrom: @Composable () -> Unit,
    country: @Composable () -> Unit,
    cast: @Composable RowScope.() -> Unit,
    directors: @Composable RowScope.() -> Unit,
    description: @Composable () -> Unit,
    trailer: @Composable () -> Unit,
    link: @Composable () -> Unit,
    rating: @Composable () -> Unit,
    purchase: @Composable () -> Unit,
    modifier: Modifier = Modifier,
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
            scrollBehavior = scrollBehavior,
            title = title,
            navigationIcon = navigationIcon,
            actions = {
                trailer()
                link()
            }
        )
    }
) { padding ->
    Box {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(.25f)
                .blur(1.pc),
            propagateMinConstraints = true
        ) {
            backdrop()
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(1.pc),
            verticalArrangement = Arrangement.spacedBy(1.pc)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(DefaultPosterAspectRatio),
                propagateMinConstraints = true
            ) {
                poster()
            }
            purchase()
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(1.pc),
                verticalArrangement = Arrangement.spacedBy(1.pc)
            ) {
                name()
                rating()
                duration()
                releasedAt()
                availableFrom()
                country()
            }
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                cast()
            }
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                directors()
            }
            description()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@PreviewFontScale
@Composable
private fun MovieScreenScaffoldPreview() = PreviewLayout {
    MovieScreenScaffold(
        title = { Text("I am movie") },
        navigationIcon = {},
        backdrop = { Box(Modifier.background(Color.Green)) },
        poster = { Box(Modifier.background(Color.Blue)) },
        name = { Text("Já jsem film") },
        duration = { Text("2h 15m") },
        releasedAt = { Text("2024") },
        availableFrom = { Text("12 Dec 2024") },
        country = { Text("CZ") },
        cast = {},
        directors = {},
        description = { Text(LoremIpsum().values.first()) },
        trailer = {},
        link = {},
        purchase = {},
        rating = { Text("74%") }
    )
}