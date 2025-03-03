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
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import androidx.compose.ui.unit.*
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import movie.metropolis.app.ui.movie.component.ActorColumn
import movie.metropolis.app.ui.movie.component.BackdropLayout
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
                .padding(2.pc)
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
                .padding(vertical = 2.pc),
            verticalArrangement = Arrangement.spacedBy(.5.pc)
        ) {
            purchase()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.pc),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    ProvideTextStyle(MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold)) {
                        name()
                    }
                }
                ProvideTextStyle(MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)) {
                    rating()
                }
            }
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.pc)
                    .alpha(.5f),
                horizontalArrangement = Arrangement.spacedBy(.5.pc),
                verticalArrangement = Arrangement.spacedBy(.5.pc)
            ) {
                ProvideTextStyle(MaterialTheme.typography.labelMedium) {
                    duration()
                    Text("•")
                    releasedAt()
                    Text("•")
                    availableFrom()
                    Text("•")
                    country()
                }
            }
            val titleMod = Modifier
                .padding(horizontal = 2.pc)
                .padding(top = 1.pc)
            Text(
                modifier = Modifier.then(titleMod),
                text = "Cast",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 1.pc, horizontal = 2.pc),
                horizontalArrangement = Arrangement.spacedBy(1.pc)
            ) {
                directors()
                Spacer(
                    Modifier
                        .background(LocalContentColor.current)
                        .fillMaxHeight()
                        .width(1.dp)
                )
                cast()
            }
            Text(
                modifier = Modifier.then(titleMod),
                text = "Description",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Box(modifier = Modifier.padding(horizontal = 2.pc)) {
                description()
            }
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
        cast = {
            repeat(5) {
                ActorColumn(
                    icon = { Box(Modifier.background(Color.Blue)) },
                    name = { Text("Já jsem herec") }
                )
            }
        },
        directors = {
            repeat(1) {
                ActorColumn(
                    icon = { Box(Modifier.background(Color.Blue)) },
                    name = { Text("Já jsem herec") }
                )
            }
        },
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