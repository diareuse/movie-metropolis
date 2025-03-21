@file:OptIn(ExperimentalSharedTransitionApi::class)

package movie.metropolis.app.ui.booking

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import dev.chrisbanes.haze.HazeState
import movie.metropolis.app.model.TimeView
import movie.metropolis.app.screen.booking.LazyTimeViewProvider
import movie.metropolis.app.screen.booking.component.ProjectionTypeRow
import movie.metropolis.app.screen.booking.component.ProjectionTypeRowDefaults
import movie.metropolis.app.ui.booking.component.BookingBox
import movie.metropolis.app.ui.booking.component.BookingTable
import movie.metropolis.app.ui.booking.component.BookingTableRow
import movie.metropolis.app.ui.booking.component.BookingTableSection
import movie.metropolis.app.ui.booking.component.DayColumn
import movie.metropolis.app.ui.booking.component.FiltersColumn
import movie.style.Image
import movie.style.layout.PreviewLayout
import movie.style.rememberImageState
import movie.style.rememberPaletteImageState
import movie.style.util.pc
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.BookingScreen(
    animationScope: AnimatedContentScope,
    state: BookingScreenState,
    onBackClick: () -> Unit,
    onTimeClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    haze: HazeState = remember { HazeState() }
) = BookingScreenScaffold(
    modifier = modifier.sharedBounds(
        rememberSharedContentState("booking"),
        animationScope,
        clipInOverlayDuringTransition = OverlayClip(MaterialTheme.shapes.medium),
        resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(contentScale = ContentScale.Crop)
    ),
    filters = { FiltersColumn(state.filters) },
    backdrop = { Image(rememberImageState(state.poster)) },
    title = { Text(state.title) },
    activeFilters = {
        if (state.activeFilterCount != 0) Badge {
            Text(state.activeFilterCount.toString())
        }
    },
    navigationIcon = {
        IconButton(onBackClick) {
            Icon(Icons.AutoMirrored.Default.ArrowBack, null)
        }
    },
    haze = haze
) { padding ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = padding.calculateTopPadding())
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 2.pc),
            horizontalArrangement = Arrangement.spacedBy(1.pc)
        ) {
            for ((index, item) in state.items.withIndex()) {
                DayColumn(
                    selected = index == state.selectedIndex,
                    text = item.dateString,
                    onClick = { state.selectedIndex = index },
                    haze = haze
                )
            }
        }
        val scroll = rememberScrollState()
        val dir = LocalLayoutDirection.current
        AnimatedContent(
            targetState = state.selectedIndex,
            transitionSpec = {
                when {
                    initialState > targetState -> slideInHorizontally { -it } togetherWith
                            slideOutHorizontally { it }

                    else -> slideInHorizontally { it } togetherWith
                            slideOutHorizontally { -it }
                }
            }
        ) {
            BookingTableContent(
                items = state.items.getOrNull(it)?.content ?: remember { mutableStateListOf() },
                defaultDuration = state.duration.run { if (this == Duration.ZERO) 1.hours else this },
                scroll = scroll,
                haze = haze,
                padding = padding,
                dir = dir,
                onTimeClick = onTimeClick
            )
        }
    }
}

@Composable
private fun BookingTableContent(
    items: SnapshotStateList<TimeView>,
    defaultDuration: Duration,
    scroll: ScrollState,
    haze: HazeState,
    padding: PaddingValues,
    dir: LayoutDirection,
    onTimeClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    BookingTable(
        modifier = modifier
            .padding(top = 1.pc),
        state = scroll,
        haze = haze,
        contentPadding = PaddingValues(
            start = padding.calculateStartPadding(dir) + 2.pc,
            end = padding.calculateEndPadding(dir) + 2.pc,
            bottom = padding.calculateBottomPadding() + 2.pc
        )
    ) {
        for (time in items) {
            val duration = when (time) {
                is TimeView.Movie -> time.movie.durationTime.run { if (this == Duration.ZERO) 1.hours else this }
                else -> defaultDuration
            }
            val image = when (time) {
                is TimeView.Movie -> time.movie.poster
                is TimeView.Cinema -> time.cinema.image
            }
            val scrollModifier = Modifier.offset { IntOffset(x = scroll.value, y = 0) }
            val imageState = rememberPaletteImageState(image)
            BookingTableSection(
                backdrop = { Image(imageState, Modifier.blur(24.dp)) },
                borderColor = imageState.palette.color
            ) {
                BookingTableRow(
                    title = {
                        when (time) {
                            is TimeView.Cinema -> Text(
                                modifier = scrollModifier,
                                text = time.cinema.name
                            )

                            is TimeView.Movie -> Text(
                                modifier = scrollModifier,
                                text = time.movie.name
                            )
                        }
                    }
                ) {
                }
                for ((type, v) in time.filteredTimes) {
                    BookingTableRow(
                        title = {
                            val locale = remember { Locale.getDefault() }
                            Row(
                                modifier = scrollModifier,
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(1.pc)
                            ) {
                                ProjectionTypeRowDefaults.Speech {
                                    Text(type.language.getDisplayLanguage(locale))
                                }
                                if (type.subtitles != null) ProjectionTypeRowDefaults.Subtitle {
                                    Text(type.subtitles.getDisplayLanguage(locale))
                                }
                                for (t in type.projection)
                                    ProjectionTypeRow(t)
                            }
                        }
                    ) {
                        for (v in v) {
                            val time = remember(v.time) {
                                Calendar.getInstance().apply { this.time = Date(v.time) }.run {
                                    get(Calendar.HOUR_OF_DAY).hours + get(Calendar.MINUTE).minutes
                                }
                            }
                            BookingBox(
                                start = time,
                                duration = duration,
                                onClick = { onTimeClick(v.formatted) },
                                time = { Text(v.formatted) }
                            )
                        }
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@PreviewFontScale
@Composable
private fun BookingScreenPreview() = PreviewLayout {
    val state = remember {
        BookingScreenState().apply {
            title = "Avatar: The Way of Water"
            items += LazyTimeViewProvider().values
        }
    }
    SharedTransitionLayout {
        AnimatedContent(state) { state ->
            BookingScreen(this, state, {}, {})
        }
    }
}