@file:OptIn(ExperimentalFoundationApi::class)

package movie.metropolis.app.screen.settings

import android.Manifest
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.text.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.*
import androidx.compose.ui.input.nestedscroll.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.launch
import movie.metropolis.app.ActivityActions
import movie.metropolis.app.LocalActivityActions
import movie.metropolis.app.R
import movie.metropolis.app.model.CalendarView
import movie.metropolis.app.screen.detail.plus
import movie.metropolis.app.screen.home.component.HomeScreenToolbar
import movie.style.AppDialog
import movie.style.AppSettings
import movie.style.InputField
import movie.style.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    behavior: TopAppBarScrollBehavior,
    viewModel: SettingsViewModel,
    contentPadding: PaddingValues
) {
    val filterSeen by viewModel.filterSeen.collectAsState()
    val addToCalendar by viewModel.addToCalendar.collectAsState()
    val calendars by viewModel.calendars.collectAsState()
    val clipRadius by viewModel.clipRadius.collectAsState()
    val onlyMovies by viewModel.onlyMovies.collectAsState()
    SettingsScreen(
        behavior = behavior,
        filterSeen = filterSeen,
        onFilterSeenChanged = viewModel::updateFilterSeen,
        addToCalendar = addToCalendar,
        onCalendarChanged = viewModel::updateCalendar,
        clipRadius = clipRadius,
        onClipRadiusChanged = viewModel::updateClipRadius,
        onlyMovies = onlyMovies,
        onOnlyMoviesChanged = viewModel::updateOnlyMovies,
        calendars = calendars.toImmutableMap(),
        contentPadding = contentPadding
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    behavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    filterSeen: Boolean = false,
    onFilterSeenChanged: (Boolean) -> Unit = {},
    addToCalendar: Boolean = false,
    onCalendarChanged: (String?) -> Unit = {},
    clipRadius: Int = 100,
    onClipRadiusChanged: (Int) -> Unit = {},
    onlyMovies: Boolean = false,
    onOnlyMoviesChanged: (Boolean) -> Unit = {},
    calendars: ImmutableMap<String, List<CalendarView>> = persistentHashMapOf(),
    contentPadding: PaddingValues = PaddingValues()
) = Scaffold(
    topBar = {
        HomeScreenToolbar(
            behavior = behavior,
            title = { Text(stringResource(id = R.string.settings)) }
        )
    }
) { padding ->
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(behavior.nestedScrollConnection),
        contentPadding = padding + contentPadding + PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
    ) {
        item {
            Text(stringResource(R.string.movies), style = Theme.textStyle.headline)
        }
        item("filter-seen") {
            FilterSeen(
                checked = filterSeen,
                onCheckedChanged = onFilterSeenChanged
            )
        }
        item("only-movies") {
            OnlyMovies(
                checked = onlyMovies,
                onCheckedChanged = onOnlyMoviesChanged
            )
        }
        item {
            Text(stringResource(R.string.tickets), style = Theme.textStyle.headline)
        }
        item("add-to-calendar") {
            Calendar(
                checked = addToCalendar,
                onSelected = onCalendarChanged,
                calendars = calendars
            )
        }
        item {
            Text(stringResource(R.string.cinemas), style = Theme.textStyle.headline)
        }
        item("clip-radius") {
            ClipRadius(
                value = clipRadius,
                onChanged = onClipRadiusChanged
            )
        }
    }
}

@Composable
fun LazyItemScope.ClipRadius(
    value: Int,
    onChanged: (Int) -> Unit
) {
    Column(
        modifier = Modifier.animateItemPlacement()
    ) {
        InputField(
            value = value.toString(),
            onValueChange = { onChanged(it.toIntOrNull() ?: 0) },
            label = { Text(stringResource(R.string.nearby_cinemas)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.nearby_cinemas_description),
            style = Theme.textStyle.caption
        )
    }
}

@Composable
fun LazyItemScope.FilterSeen(
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit
) {
    AppSettings(
        modifier = Modifier.animateItemPlacement(),
        checked = checked,
        onCheckedChanged = onCheckedChanged,
        title = { Text(stringResource(R.string.settings_unseen_title)) },
        description = { Text(stringResource(R.string.settings_unseen_description)) }
    )
}

@Composable
fun LazyItemScope.OnlyMovies(
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit
) {
    AppSettings(
        modifier = Modifier.animateItemPlacement(),
        checked = checked,
        onCheckedChanged = onCheckedChanged,
        title = { Text(stringResource(R.string.settings_only_movies_title)) },
        description = { Text(stringResource(R.string.settings_only_movies_description)) }
    )
}

@Composable
fun LazyItemScope.Calendar(
    checked: Boolean,
    calendars: ImmutableMap<String, List<CalendarView>>,
    onSelected: (String?) -> Unit,
    actions: ActivityActions = LocalActivityActions.current
) {
    val scope = rememberCoroutineScope()
    var isVisible by rememberSaveable { mutableStateOf(false) }
    fun toggle() {
        if (checked) return onSelected(null)
        scope.launch {
            val permissions = arrayOf(
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR
            )
            if (!actions.requestPermissions(permissions)) {
                return@launch
            }
            if (calendars.size == 1) onSelected(calendars.entries.first().key)
            else isVisible = true
        }
    }
    AppSettings(
        modifier = Modifier.animateItemPlacement(),
        checked = checked,
        onCheckedChanged = { toggle() },
        title = { Text(stringResource(R.string.settings_calendar_title)) },
        description = { Text(stringResource(R.string.settings_calendar_description)) }
    )
    AppDialog(
        isVisible = isVisible,
        onVisibilityChanged = { isVisible = it }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(24.dp)
                    .background(Theme.color.container.background, Theme.container.card),
                contentPadding = PaddingValues(24.dp)
            ) {
                item("title") {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            stringResource(R.string.select_calendar_title),
                            style = Theme.textStyle.title
                        )
                        Text(
                            stringResource(R.string.select_calendar_description),
                            style = Theme.textStyle.caption
                        )
                        HorizontalDivider()
                    }
                }
                for ((key, items) in calendars.entries) {
                    item {
                        Text(key)
                    }
                    items(items, key = { it.id }) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelected(it.id)
                                    isVisible = false
                                }
                                .padding(vertical = 8.dp)
                        ) {
                            Text(text = it.name)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        SettingsScreen()
    }
}