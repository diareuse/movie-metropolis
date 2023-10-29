package movie.metropolis.app.screen.listing.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.screen.listing.MovieViewProvider
import movie.style.Image
import movie.style.layout.PreviewLayout
import movie.style.rememberPaletteImageState

@Composable
fun MovieItemActive(
    view: MovieView,
    onClick: () -> Unit,
    onLongPress: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberPaletteImageState(url = view.poster?.url.orEmpty())
    MovieItemLayout(
        modifier = modifier,
        shadowColor = state.palette.color,
        posterAspectRatio = view.poster?.aspectRatio ?: DefaultPosterAspectRatio,
        posterOverlay = {
            val rating = view.rating
            if (rating != null) Text(rating, Modifier.padding(horizontal = 12.dp, vertical = 8.dp))
        }
    ) {
        Image(
            modifier = Modifier
                .clickable(onClick = onClick)
                .detectLongPress(onLongPress),
            state = state
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun MovieItemActivePreview(
    @PreviewParameter(MovieViewProvider::class, 1)
    parameter: MovieView
) = PreviewLayout {
    MovieItemActive(parameter, {}, {})
}