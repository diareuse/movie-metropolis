@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)

package movie.metropolis.app.screen.settings

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import movie.style.rememberImageState

@Composable
fun SettingsScreen(
    background: String,
    state: SettingsState,
    onStateChange: (SettingsState) -> Unit,
    onClickBack: () -> Unit,
    onShowCalendarsRequest: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
) = Scaffold(
    modifier = modifier,
    topBar = {
        CollapsingTopAppBar(
            modifier = Modifier.alignForLargeScreen(),
            title = { Text("Settings") },
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
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painterResource(R.drawable.ic_location), null)
                CommonTextField(
                    modifier = Modifier.weight(1f),
                    value = state.nearbyCinemas,
                    onValueChange = { onStateChange(state.copy(nearbyCinemas = it)) },
                    visualTransformation = LengthVisualTransformation,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = clearFocus())
                )
            }
        }
    }

}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun SettingsScreenPreview() = PreviewLayout {
    SettingsScreen(
        background = "",
        state = SettingsState(),
        onStateChange = {},
        onClickBack = {},
        onShowCalendarsRequest = {}
    )
}