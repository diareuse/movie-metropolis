@file:OptIn(ExperimentalMaterial3Api::class)

package movie.metropolis.app.screen.space

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.R
import movie.metropolis.app.model.DiskSpace.Companion.bytes
import movie.metropolis.app.screen.card.component.ScatterPointBackground
import movie.metropolis.app.screen.settings.component.SettingsItemRow
import movie.metropolis.app.screen.settings.component.SettingsSection
import movie.style.CollapsingTopAppBar
import movie.style.layout.PreviewLayout
import movie.style.layout.alignForLargeScreen

@Composable
fun SpaceScreen(
    state: SpaceState,
    onClickBack: () -> Unit,
    onDeleteMoviesClick: () -> Unit,
    onDeletePosterClick: () -> Unit,
    onDeleteRatingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
) = Scaffold(
    modifier = modifier,
    topBar = {
        CollapsingTopAppBar(
            modifier = Modifier.alignForLargeScreen(),
            title = { Text(stringResource(R.string.space_management)) },
            navigationIcon = {
                IconButton(onClick = onClickBack) {
                    Icon(painterResource(R.drawable.ic_close), null)
                }
            },
            scrollBehavior = scrollBehavior
        )
    }
) { padding ->
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .alpha(.3f),
        propagateMinConstraints = true
    ) {
        ScatterPointBackground(
            bounds = DpRect(DpOffset.Zero, size = DpSize(maxWidth, maxHeight)),
            count = 50
        )
    }
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(padding)
            .padding(24.dp)
            .imePadding()
            .navigationBarsPadding()
            .alignForLargeScreen(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SettingsSection(title = { Text(stringResource(R.string.images)) }) {
            SettingsItemRow(
                title = { Text(stringResource(R.string.posters)) },
                description = { Text(stringResource(R.string.mb_used, state.posters.megaBytes)) },
                value = {
                    IconButton(onClick = onDeletePosterClick) {
                        Icon(painterResource(id = R.drawable.ic_delete), null)
                    }
                }
            )
        }
        SettingsSection(title = { Text(stringResource(R.string.database)) }) {
            SettingsItemRow(
                title = { Text(stringResource(R.string.movies_and_tickets)) },
                description = { Text(stringResource(R.string.mb_used, state.movies.megaBytes)) },
                value = {
                    IconButton(onClick = onDeleteMoviesClick) {
                        Icon(painterResource(id = R.drawable.ic_delete), null)
                    }
                }
            )
            SettingsItemRow(
                title = { Text(stringResource(R.string.ratings_and_metadata)) },
                description = { Text(stringResource(R.string.mb_used, state.ratings.megaBytes)) },
                value = {
                    IconButton(onClick = onDeleteRatingsClick) {
                        Icon(painterResource(id = R.drawable.ic_delete), null)
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun SpaceScreenPreview() = PreviewLayout {
    SpaceScreen(
        state = SpaceState(1000000L.bytes, 2000000L.bytes, 3123123L.bytes),
        onClickBack = {},
        onDeleteMoviesClick = {},
        onDeletePosterClick = {},
        onDeleteRatingsClick = {}
    )
}

