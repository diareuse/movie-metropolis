@file:OptIn(ExperimentalFoundationApi::class)

package movie.metropolis.app.screen.listing.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import movie.style.layout.DefaultPosterAspectRatio
import movie.style.layout.PreviewLayout
import movie.style.modifier.surface
import movie.style.theme.Theme

@Composable
fun PosterColumn(
    color: Color,
    poster: @Composable () -> Unit,
    name: @Composable () -> Unit,
    rating: @Composable () -> Unit,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) = RatedPoster(
    modifier = modifier,
    color = color,
    poster = poster,
    rating = rating,
    onClick = onClick,
    onLongClick = onLongClick,
    name = name
)

fun Color.vivid(): Color {
    val rgb = this.toArgb()
    val red = rgb.red
    val green = rgb.green
    val blue = rgb.blue
    val hsv = FloatArray(3)
    android.graphics.Color.RGBToHSV(red, green, blue, hsv)
    return Color.hsv(hsv[0], 1f, 1f)
}

@Composable
private fun RatedPoster(
    color: Color,
    poster: @Composable () -> Unit,
    rating: @Composable () -> Unit,
    name: @Composable () -> Unit,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val baseline = Theme.container.poster
    val containerColor = Theme.color.container.background
    Box(
        modifier = modifier
            .combinedClickable(onClick = onClick, onLongClick = onLongClick, role = Role.Image)
            .surface(containerColor, baseline, 16.dp, color.vivid())
            .aspectRatio(DefaultPosterAspectRatio),
        propagateMinConstraints = true
    ) {
        val haze = remember { HazeState() }
        Box(
            modifier = Modifier.hazeSource(haze), propagateMinConstraints = true
        ) {
            poster()
        }
        Box(
            modifier = Modifier
                .wrapContentSize(Alignment.TopEnd)
                .padding(4.dp)
                .hazeEffect(
                    haze, HazeDefaults.style(
                        backgroundColor = color.copy(.25f),
                        blurRadius = 10.dp,
                        noiseFactor = 5f
                    )
                )
        ) {
            rating()
        }
        Box(
            modifier = Modifier
                .wrapContentHeight(Alignment.Bottom)
                .heightIn(min = 32.dp)
                .padding(4.dp)
                .hazeEffect(
                    haze, HazeDefaults.style(
                        backgroundColor = color.copy(.25f),
                        blurRadius = 10.dp,
                        noiseFactor = 5f
                    )
                )
                .padding(vertical = 8.dp, horizontal = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            ProvideTextStyle(
                Theme.textStyle.caption.copy(
                    textAlign = TextAlign.Center,
                    fontSize = 10.sp,
                    lineHeight = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            ) {
                CompositionLocalProvider(LocalContentColor provides color.contentColor) {
                    name()
                }
            }
        }
    }
}

@Stable
private fun Modifier.fadingEdge(
    topEdgeMaxHeight: Dp = 0.dp,
    bottomEdgeMaxHeight: Dp = 0.dp
) = then(
    Modifier
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()

            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black),
                    startY = 0f,
                    endY = topEdgeMaxHeight.toPx(),
                ),
                blendMode = BlendMode.DstIn,
            )

            val bottomEdgeHeightPx = bottomEdgeMaxHeight.toPx()
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Black, Color.Transparent),
                    startY = size.height - bottomEdgeHeightPx,
                    endY = size.height,
                ),
                blendMode = BlendMode.DstIn,
            )
        }
)

val Color.contentColor inline get() = if (luminance() > .5f) Color.Black else Color.White

@Composable
fun RatingBox(
    color: Color,
    rating: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = Theme.textStyle.caption.copy(fontWeight = FontWeight.Medium),
    offset: PaddingValues = PaddingValues(start = 4.dp, top = 4.dp),
    padding: PaddingValues = PaddingValues(8.dp, 4.dp),
    shape: Shape = CircleShape
) = Box(
    modifier = modifier
        //.padding(offset)
        //.surface(color, shape, 16.dp, color)
        .padding(padding)
) {
    ProvideTextStyle(textStyle) {
        CompositionLocalProvider(LocalContentColor provides color.contentColor) {
            rating()
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun PosterColumnPreview() = PreviewLayout(
    modifier = Modifier
        .padding(16.dp)
        .width(100.dp)
) {
    PosterColumn(
        color = Color.Gray,
        poster = { Box(Modifier.background(Color.Green)) },
        name = { Text("Movie name") },
        rating = {
            RatingBox(
                color = Color.Gray,
                rating = { Text("86%") }
            )
        },
        onClick = {},
        onLongClick = {}
    )
}