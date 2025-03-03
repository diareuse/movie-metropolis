package movie.metropolis.app.ui.movie

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import androidx.compose.ui.unit.*
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
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
    haze: HazeState = remember { HazeState() }
) = Scaffold(
    modifier = modifier,
    topBar = {
        Row(
            modifier = Modifier
                .padding(1.pc)
                .statusBarsPadding()
        ) {
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .hazeEffect(haze)
            ) {
                navigationIcon()
            }
            Spacer(Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .hazeEffect(haze)
            ) {
                trailer()
                link()
            }
        }
    }
) { padding ->
    val scrollState = rememberScrollState()
    BackdropLayout(
        modifier = Modifier.hazeSource(haze),
        backdrop = {
            val color = MaterialTheme.colorScheme.background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(DefaultPosterAspectRatio)
                    .drawWithCache {
                        val scroll = scrollState.value
                        val height = minOf(scrollState.maxValue, scrollState.viewportSize)
                        var progress = 1f * scroll.coerceAtMost(height) / height
                        progress = progress.coerceAtMost(.8f)
                        val brush = Brush.verticalGradient(
                            listOf(color, color.copy(0f)),
                            startY = size.height,
                            endY = 128.dp.toPx()
                        )
                        val color = color.copy(progress)
                        onDrawWithContent {
                            drawContent()
                            drawRect(brush)
                            drawRect(color)
                        }
                    },
                propagateMinConstraints = true
            ) {
                poster()
            }
        },
        offset = (padding.calculateTopPadding() + padding.calculateBottomPadding()) + 128.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(padding)
                .padding(it)
                .padding(1.pc),
            verticalArrangement = Arrangement.spacedBy(1.pc)
        ) {
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
        navigationIcon = {
            IconButton({}) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, null)
            }
        },
        backdrop = {
            Box(
                Modifier.background(
                    Brush.sweepGradient(
                        listOf(
                            Color.Green,
                            Color.Yellow,
                            Color.Blue,
                            Color.Magenta
                        )
                    )
                )
            )
        },
        poster = { Box(Modifier.background(Color.Blue)) },
        name = { Text("Já jsem film") },
        duration = { Text("2h 15m") },
        releasedAt = { Text("2024") },
        availableFrom = { Text("12 Dec 2024") },
        country = { Text("CZ") },
        cast = {},
        directors = {},
        description = { Text(LoremIpsum().values.first()) },
        trailer = {
            IconButton({}) {
                Icon(Icons.Default.PlayArrow, null)
            }
        },
        link = {
            IconButton({}) {
                Icon(Icons.Default.Info, null)
            }
        },
        purchase = {},
        rating = { Text("74%") }
    )
}