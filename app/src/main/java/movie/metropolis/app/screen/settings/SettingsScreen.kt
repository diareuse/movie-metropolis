package movie.metropolis.app.screen.settings

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import movie.metropolis.app.R
import movie.metropolis.app.screen.detail.plus
import movie.style.AppDialog
import movie.style.AppIconButton
import movie.style.AppToolbar
import movie.style.haptic.hapticClick
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
    SettingsScreen(
        filterSeen = filterSeen,
        onFilterSeenChanged = viewModel::updateFilterSeen,
        addToCalendar = addToCalendar,
        onCalendarChanged = viewModel::updateCalendar,
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
                    Text("Settings")
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = padding + PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
        ) {
            FilterSeen(
                checked = filterSeen,
                onCheckedChanged = onFilterSeenChanged
            )
            Calendar(
                checked = addToCalendar,
                onSelected = onCalendarChanged,
                calendars = calendars,
                onPermissionsRequested = onPermissionsRequested
            )
            item("notice") {
                Text(
                    modifier = Modifier.navigationBarsPadding(),
                    text = "Some settings may require for you to close the app completely and start again to apply.",
                    style = Theme.textStyle.caption,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Suppress("FunctionName")
fun LazyListScope.FilterSeen(
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit
) = item("filter-seen") {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Theme.container.button)
            .clickable(onClick = hapticClick { onCheckedChanged(!checked) })
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChanged
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Show unseen only",
                style = Theme.textStyle.body,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "If checked, app will show only movies that are not present in the Tickets section.",
                style = Theme.textStyle.caption
            )
        }
    }
}

@Suppress("FunctionName")
fun LazyListScope.Calendar(
    checked: Boolean,
    calendars: Map<String, String>,
    onSelected: (String?) -> Unit,
    onPermissionsRequested: suspend (Array<String>) -> Boolean
) = item("add-to-calendar") {
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Theme.container.button)
            .clickable(onClick = hapticClick { toggle() })
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { toggle() }
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Add Tickets to Calendar",
                style = Theme.textStyle.body,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "If checked, app will automatically add all of your tickets to calendar of your choosing. It will do so for past and future tickets.",
                style = Theme.textStyle.caption
            )
        }
    }
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
                        Text("Select a calendar", style = Theme.textStyle.title)
                        Text(
                            "Events will be queried and created in the selected calendar. You may want to create a special calendar in order to not interfere with existing events.",
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
            onBackClick = {},
            onPermissionsRequested = { false }
        )
    }
}