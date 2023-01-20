@file:OptIn(ExperimentalFoundationApi::class)

package movie.metropolis.app.screen.settings

import android.Manifest
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import movie.metropolis.app.R
import movie.metropolis.app.screen.detail.plus
import movie.style.AppDialog
import movie.style.AppIconButton
import movie.style.AppSettings
import movie.style.AppToolbar
import movie.style.InputField
import movie.style.theme.Theme

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onPermissionsRequested: suspend (Array<String>) -> Boolean
) {
    val filterSeen by viewModel.filterSeen.collectAsState()
    val addToCalendar by viewModel.addToCalendar.collectAsState()
    val calendars by viewModel.calendars.collectAsState()
    val clipRadius by viewModel.clipRadius.collectAsState()
    SettingsScreen(
        filterSeen = filterSeen,
        onFilterSeenChanged = viewModel::updateFilterSeen,
        addToCalendar = addToCalendar,
        onCalendarChanged = viewModel::updateCalendar,
        clipRadius = clipRadius,
        onClipRadiusChanged = viewModel::updateClipRadius,
        calendars = calendars,
        onBackClick = onBackClick,
        onPermissionsRequested = {
            val result = onPermissionsRequested(it)
            // abuse reactivity to refresh
            viewModel.updateFilterSeen(filterSeen)
            result
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    filterSeen: Boolean,
    onFilterSeenChanged: (Boolean) -> Unit,
    addToCalendar: Boolean,
    onCalendarChanged: (String?) -> Unit,
    clipRadius: Int,
    onClipRadiusChanged: (Int) -> Unit,
    calendars: Map<String, String>,
    onPermissionsRequested: suspend (Array<String>) -> Boolean,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            AppToolbar(
                navigationIcon = {
                    AppIconButton(
                        onClick = onBackClick,
                        painter = painterResource(id = R.drawable.ic_back)
                    )
                },
                title = {
                    Text(stringResource(R.string.settings))
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = padding + PaddingValues(24.dp),
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
            item {
                Text(stringResource(R.string.tickets), style = Theme.textStyle.headline)
            }
            item("add-to-calendar") {
                Calendar(
                    checked = addToCalendar,
                    onSelected = onCalendarChanged,
                    calendars = calendars,
                    onPermissionsRequested = onPermissionsRequested
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
            label = stringResource(R.string.nearby_cinemas),
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
fun LazyItemScope.Calendar(
    checked: Boolean,
    calendars: Map<String, String>,
    onSelected: (String?) -> Unit,
    onPermissionsRequested: suspend (Array<String>) -> Boolean
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
            if (!onPermissionsRequested(permissions)) {
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
                        Divider()
                    }
                }
                items(calendars.toList(), key = { (id) -> id }) { (id, name) ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelected(id)
                                isVisible = false
                            }
                            .padding(vertical = 8.dp)
                    ) {
                        Text(text = name)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        SettingsScreen(
            filterSeen = true,
            onFilterSeenChanged = {},
            addToCalendar = false,
            onCalendarChanged = {},
            calendars = emptyMap(),
            clipRadius = 100,
            onClipRadiusChanged = {},
            onBackClick = {},
            onPermissionsRequested = { false }
        )
    }
}