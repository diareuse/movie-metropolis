package movie.metropolis.app.screen.listing.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.screen.detail.component.FavoriteButton
import movie.metropolis.app.screen.listing.MovieViewProvider
import movie.style.Image
import movie.style.layout.PreviewLayout
import movie.style.rememberPaletteImageState

@Composable
fun MovieItemUpcoming(
    view: MovieView,
    onClick: () -> Unit,
    onClickFavorite: () -> Unit,
    onLongPress: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberPaletteImageState(url = view.poster?.url.orEmpty())
    MovieItemLayout(
        shadowColor = state.palette.color,
        modifier = modifier,
        posterAspectRatio = view.poster?.aspectRatio ?: DefaultPosterAspectRatio,
        textPadding = PaddingValues(top = 8.dp),
        text = {
            MovieSubText(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = view.availableFrom
            )
        },
        posterOverlay = {
            FavoriteButton(
                isChecked = view.favorite,
                onClick = onClickFavorite,
                tintChecked = LocalContentColor.current
            )
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
private fun MovieItemUpcomingPreview(
    @PreviewParameter(MovieViewProvider::class, 1)
    parameter: MovieView
) = PreviewLayout {
    MovieItemUpcoming(parameter, {}, {}, {})
}