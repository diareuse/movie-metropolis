@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalFoundationApi::class)

package movie.metropolis.app.screen2.cinema

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.screen.cinema.component.CinemaViewParameter
import movie.metropolis.app.screen2.cinema.component.CinemaBox
import movie.metropolis.app.screen2.cinema.component.PermissionBox
import movie.metropolis.app.screen2.listing.component.RatingBox
import movie.metropolis.app.util.rememberVisibleItemAsState
import movie.style.Image
import movie.style.layout.PreviewLayout
import movie.style.layout.plus
import movie.style.rememberImageState
import movie.style.rememberPaletteImageState

@Composable
fun CinemasScreen(
    cinemas: ImmutableList<CinemaView>,
    state: LazyListState,
    onClick: (CinemaView) -> Unit,
    onMapClick: (CinemaView) -> Unit,
    modifier: Modifier = Modifier,
    permission: MultiplePermissionsState = rememberMultiplePermissionsState(permissions = emptyList()),
    contentPadding: PaddingValues = PaddingValues()
) = Box(modifier = modifier.fillMaxSize(), propagateMinConstraints = true) {
    val selectedItem by state.rememberVisibleItemAsState()
    val background = rememberImageState(cinemas.getOrNull(selectedItem)?.image)
    AnimatedContent(
        targetState = background,
        transitionSpec = { fadeIn() togetherWith fadeOut() }
    ) {
        Image(
            modifier = Modifier
                .blur(16.dp)
                .alpha(.35f),
            state = it
        )
    }
    LazyColumn(
        state = state,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = contentPadding + PaddingValues(24.dp)
    ) {
        if (!permission.allPermissionsGranted) item {
            PermissionBox(
                modifier = Modifier.animateItemPlacement(),
                icon = { Icon(Icons.Rounded.LocationOn, null) },
                title = { Text("Location Permission") },
                message = { Text("The app uses location permission to sort this list by the distance from any given cinema. The location is processed ephemerally on device and never leaves it.") },
                onClick = permission::launchMultiplePermissionRequest
            )
        }
        items(cinemas) {
            val state = rememberPaletteImageState(url = it.image)
            CinemaBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 128.dp),
                color = state.palette.color,
                contentColor = state.palette.textColor,
                name = { Text(it.name) },
                distance = {
                    val distance = it.distance
                    if (distance != null) RatingBox(
                        color = state.palette.color,
                        contentColor = state.palette.textColor,
                        rating = { Text(distance) },
                        offset = PaddingValues(start = 4.dp, bottom = 4.dp)
                    )
                },
                poster = { Image(state) },
                action = { Icon(Icons.Rounded.LocationOn, null) },
                onClick = { onClick(it) },
                onActionClick = { onMapClick(it) }
            )
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun CinemasScreenPreview() = PreviewLayout {
    val cinemas = CinemaViewParameter().values.toImmutableList()
    CinemasScreen(
        cinemas = cinemas,
        state = rememberLazyListState(),
        onClick = {},
        onMapClick = {}
    )
}