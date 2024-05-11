@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)

package movie.metropolis.app.screen.settings

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import movie.metropolis.app.R
import movie.metropolis.app.screen.profile.CommonSwitch
import movie.metropolis.app.screen.profile.CommonTextField
import movie.metropolis.app.screen.settings.component.LengthVisualTransformation
import movie.metropolis.app.screen.settings.component.SettingsItemColumn
import movie.metropolis.app.screen.settings.component.SettingsItemRow
import movie.metropolis.app.screen.settings.component.SettingsSection
import movie.style.BackgroundImage
import movie.style.CollapsingTopAppBar
import movie.style.action.clearFocus
import movie.style.layout.PreviewLayout
import movie.style.layout.alignForLargeScreen
import movie.style.modifier.surface
import movie.style.rememberImageState
import movie.style.theme.Theme

@Composable
fun SettingsScreen(
    background: String,
    state: SettingsState,
    onStateChange: (SettingsState) -> Unit,
    onClickBack: () -> Unit,
    onShowCalendarsRequest: () -> Unit,
    onAddFilterClick: () -> Unit,
    onDeleteFilterClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
) = Scaffold(
    modifier = modifier,
    topBar = {
        CollapsingTopAppBar(
            modifier = Modifier.alignForLargeScreen(),
            title = { Text(stringResource(id = R.string.settings)) },
            navigationIcon = {
                IconButton(onClick = onClickBack) {
                    Icon(painterResource(R.drawable.ic_back), null)
                }
            },
            scrollBehavior = scrollBehavior
        )
    }
) { padding ->
    BackgroundImage(
        modifier = Modifier.fillMaxSize(),
        state = rememberImageState(background)
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .verticalScroll(rememberScrollState())
            .padding(padding)
            .padding(24.dp)
            .imePadding()
            .navigationBarsPadding()
            .alignForLargeScreen(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        SettingsSection(title = { Text(stringResource(id = R.string.movies)) }) {
            SettingsItemRow(
                title = { Text(stringResource(id = R.string.settings_unseen_title)) },
                description = { Text(stringResource(id = R.string.settings_unseen_description)) },
                value = {
                    CommonSwitch(
                        checked = state.unseenOnly,
                        onCheckedChange = { onStateChange(state.copy(unseenOnly = it)) }
                    )
                }
            )
            SettingsItemRow(
                title = { Text(stringResource(id = R.string.settings_only_movies_title)) },
                description = { Text(stringResource(id = R.string.settings_only_movies_description)) },
                value = {
                    CommonSwitch(
                        checked = state.moviesOnly,
                        onCheckedChange = { onStateChange(state.copy(moviesOnly = it)) }
                    )
                }
            )
            SettingsItemRow(
                title = { Text(stringResource(R.string.filter_movie_names)) },
                description = { Text(stringResource(R.string.filter_movie_names_description)) },
                value = {}
            )
            Column(
                Modifier
                    .padding(start = 16.dp)
                    .surface(Theme.color.container.surface.copy(.1f), Theme.container.button)
            ) {
                var expanded by rememberSaveable { mutableStateOf(false) }
                CommonTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = state.pendingFilter,
                    onValueChange = { onStateChange(state.copy(pendingFilter = it)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    trailingIcon = {
                        if (state.pendingFilter.isNotBlank()) IconButton(onClick = onAddFilterClick) {
                            Icon(painterResource(R.drawable.ic_add), null)
                        }
                    },
                    leadingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            val painter = when (expanded) {
                                true -> painterResource(R.drawable.ic_collapse)
                                else -> painterResource(R.drawable.ic_expand)
                            }
                            Icon(painter, null)
                        }
                    },
                    label = { Text(stringResource(R.string.keyword)) }
                )
                AnimatedVisibility(expanded) {
                    Column {
                        for ((index, filter) in state.filters.withIndex()) {
                            Row(
                                modifier = Modifier.padding(start = 32.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(filter, Modifier.weight(1f))
                                IconButton(onClick = { onDeleteFilterClick(filter) }) {
                                    Icon(painterResource(R.drawable.ic_delete), null)
                                }
                            }
                            if (index != state.filters.size - 1) HorizontalDivider(
                                modifier = Modifier.padding(
                                    start = 32.dp,
                                    end = 48.dp
                                )
                            )
                        }
                    }
                }
            }
        }
        SettingsSection(title = { Text(stringResource(id = R.string.tickets)) }) {
            SettingsItemRow(
                title = { Text(stringResource(id = R.string.settings_calendar_title)) },
                description = { Text(stringResource(id = R.string.settings_calendar_description)) },
                value = {
                    CommonSwitch(
                        checked = state.tickets,
                        onCheckedChange = { onShowCalendarsRequest() }
                    )
                }
            )
        }
        SettingsSection(title = { Text(stringResource(id = R.string.cinemas)) }) {
            SettingsItemColumn(
                title = { Text(stringResource(id = R.string.nearby_cinemas)) },
                description = { Text(stringResource(id = R.string.nearby_cinemas_description)) },
                value = {}
            )
            CommonTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                value = state.nearbyCinemas,
                onValueChange = { onStateChange(state.copy(nearbyCinemas = it)) },
                visualTransformation = LengthVisualTransformation,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                leadingIcon = {
                    Icon(painterResource(R.drawable.ic_location), null)
                },
                keyboardActions = KeyboardActions(onDone = clearFocus())
            )
        }
    }

}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, heightDp = 1300)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO, heightDp = 1300)
@Composable
private fun SettingsScreenPreview() = PreviewLayout {
    SettingsScreen(
        background = "",
        state = SettingsState(filters = listOf("opera", "opereta")),
        onStateChange = {},
        onClickBack = {},
        onShowCalendarsRequest = {},
        onAddFilterClick = {},
        onDeleteFilterClick = {}
    )
}