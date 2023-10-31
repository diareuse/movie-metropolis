@file:OptIn(ExperimentalFoundationApi::class)

package movie.metropolis.app.screen2.listing.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.screen.listing.component.DefaultPosterAspectRatio
import movie.style.layout.PreviewLayout
import movie.style.modifier.LightSource
import movie.style.modifier.glow
import movie.style.modifier.surface
import movie.style.shape.CompositeShape
import movie.style.shape.CutoutShape
import movie.style.theme.Theme

@Composable
fun PosterColumn(
    color: Color,
    contentColor: Color,
    poster: @Composable () -> Unit,
    favorite: @Composable () -> Unit,
    name: @Composable () -> Unit,
    rating: @Composable () -> Unit,
    onClick: () -> Unit,
    onActionClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(8.dp),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    RatedPoster(
        color = color,
        contentColor = contentColor,
        poster = poster,
        rating = rating,
        action = favorite,
        onClick = onClick,
        onActionClick = onActionClick,
        onLongClick = onLongClick
    )
    ProvideTextStyle(
        Theme.textStyle.caption.copy(
            textAlign = TextAlign.Center,
            fontSize = 10.sp,
            lineHeight = 10.sp
        )
    ) {
        name()
    }
}


@Composable
private fun RatedPoster(
    color: Color,
    contentColor: Color,
    poster: @Composable () -> Unit,
    rating: @Composable () -> Unit,
    action: @Composable () -> Unit,
    onClick: () -> Unit,
    onActionClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    var cutoutSize by remember { mutableStateOf(DpSize.Zero) }
    val favoriteSize = DpSize(36.dp, 36.dp)
    val shape = CompositeShape {
        setBaseline(Theme.container.poster)
        addShape(
            shape = CutoutShape(
                Theme.container.poster.topStart,
                CutoutShape.Orientation.BottomRight
            ),
            size = cutoutSize,
            alignment = Alignment.BottomEnd,
            operation = PathOperation.Difference
        )
        addShape(
            shape = CutoutShape(
                Theme.container.poster.topStart,
                CutoutShape.Orientation.TopLeft
            ),
            size = favoriteSize,
            alignment = Alignment.TopStart,
            operation = PathOperation.Difference
        )
    }
    val containerColor = Theme.color.container.background
    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .surface(containerColor, shape, 16.dp, color)
                .combinedClickable(onClick = onClick, onLongClick = onLongClick, role = Role.Image)
                .glow(shape)
                .aspectRatio(DefaultPosterAspectRatio),
            propagateMinConstraints = true
        ) {
            poster()
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .onSizeChanged {
                    cutoutSize = with(density) { DpSize(it.width.toDp(), it.height.toDp()) }
                }
        ) {
            rating()
        }
        Box(
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.TopStart)
                .surface(color, CircleShape, 16.dp, color)
                .clickable(onClick = onActionClick, role = Role.Button)
                .glow(
                    CircleShape,
                    lightSource = LightSource.BottomRight,
                    color = contentColor,
                    width = 2.dp,
                    alpha = .4f
                )
                .padding(6.dp),
            propagateMinConstraints = true
        ) {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                action()
            }
        }
    }
}

@Composable
fun RatingBox(
    color: Color,
    contentColor: Color,
    rating: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    offset: PaddingValues = PaddingValues(start = 4.dp, top = 4.dp)
) = Box(
    modifier = modifier
        .padding(offset)
        .surface(color, CircleShape, 16.dp, color)
        .glow(CircleShape, contentColor, width = 2.dp)
        .padding(8.dp, 4.dp)
) {
    ProvideTextStyle(Theme.textStyle.caption.copy(fontWeight = FontWeight.Medium)) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
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
        contentColor = Color.White,
        poster = { Box(Modifier.background(Color.Gray)) },
        favorite = { Icon(Icons.Rounded.Favorite, null) },
        name = { Text("Movie name") },
        rating = {
            RatingBox(
                color = Color.Gray,
                contentColor = Color.White,
                rating = { Text("86%") }
            )
        },
        onClick = {},
        onActionClick = {},
        onLongClick = {}
    )
}

private class PosterColumnParameter : PreviewParameterProvider<PosterColumnParameter.Data> {
    override val values = sequence { yield(Data()) }

    class Data
}