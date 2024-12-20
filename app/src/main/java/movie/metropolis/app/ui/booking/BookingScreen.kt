package movie.metropolis.app.ui.booking

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.model.TimeView
import movie.metropolis.app.screen.booking.LazyTimeViewProvider
import movie.metropolis.app.screen.booking.component.ProjectionTypeRow
import movie.metropolis.app.screen.booking.component.ProjectionTypeRowDefaults
import movie.metropolis.app.ui.booking.component.FiltersColumn
import movie.style.Image
import movie.style.layout.PreviewLayout
import movie.style.layout.plus
import movie.style.rememberImageState
import movie.style.util.pc
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    state: BookingScreenState,
    onBackClick: () -> Unit,
    onTimeClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) = BookingScreenScaffold(
    modifier = modifier,
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
    }
) { padding ->
    val locale = remember { Locale.getDefault() }
    val horizontalPadding = Modifier.padding(horizontal = 1.pc)
    LazyColumn(
        contentPadding = padding + PaddingValues(vertical = 1.pc),
        verticalArrangement = Arrangement.spacedBy(1.pc)
    ) {
        for (times in state.items) {
            // fixme replace with a time-table view
            if (times.isEmpty) continue
            item { Text(times.dateString, Modifier.then(horizontalPadding)) }
            for (time in times.content) {
                if (time.filteredTimes.isEmpty()) continue
                when (time) {
                    is TimeView.Cinema -> item {
                        Column(Modifier.then(horizontalPadding)) {
                            Text(time.cinema.name)
                            Image(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp),
                                state = rememberImageState(time.cinema.image)
                            )
                        }
                    }

                    is TimeView.Movie -> item {
                        Column(Modifier.then(horizontalPadding)) {
                            Text(time.movie.name)
                            Image(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp),
                                state = rememberImageState(time.movie.poster?.url)
                            )
                        }
                    }
                }
                for ((type, v) in time.filteredTimes) item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(1.pc),
                        verticalAlignment = Alignment.CenterVertically,
                        contentPadding = PaddingValues(horizontal = 1.pc)
                    ) {
                        item {
                            ProjectionTypeRow(
                                modifier = Modifier.fillMaxWidth(),
                                language = {
                                    ProjectionTypeRowDefaults.Speech {
                                        Text(type.language.getDisplayLanguage(locale))
                                    }
                                },
                                subtitle = {
                                    if (type.subtitles != null) ProjectionTypeRowDefaults.Subtitle {
                                        Text(type.subtitles.getDisplayLanguage(locale))
                                    }
                                }
                            ) {
                                for (p in type.projection)
                                    ProjectionTypeRow(type = p)
                            }
                        }
                        items(v) {
                            Button({ onTimeClick(it.url) }) {
                                Text(it.formatted)
                            }
                        }
                    }
                }
            }
        }
    }
}

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
    BookingScreen(state, {}, {})
}