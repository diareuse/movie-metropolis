package movie.metropolis.app.screen.listing.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import movie.style.layout.CutoutLayout
import movie.style.theme.Theme

@Composable
fun MovieItemLayout(
    modifier: Modifier = Modifier,
    shadowColor: Color = Color.Black,
    text: @Composable (ColumnScope.() -> Unit)? = null,
    posterOverlay: @Composable () -> Unit = {},
    posterAspectRatio: Float = DefaultPosterAspectRatio,
    height: Dp = 225.dp,
    textPadding: PaddingValues = PaddingValues(
        top = 16.dp,
        start = 12.dp,
        end = 12.dp,
        bottom = 0.dp
    ),
    poster: @Composable () -> Unit
) {
    Column(modifier = modifier.width(IntrinsicSize.Min)) {
        Row(
            modifier = Modifier.height(height),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CutoutLayout(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(posterAspectRatio),
                color = shadowColor,
                shape = Theme.container.poster,
                overlay = posterOverlay,
                content = poster
            )
        }
        if (text != null) Column(
            modifier = Modifier
                .padding(textPadding)
                .fillMaxWidth(),
            content = text
        )
    }
}

const val DefaultPosterAspectRatio = 0.67375886f