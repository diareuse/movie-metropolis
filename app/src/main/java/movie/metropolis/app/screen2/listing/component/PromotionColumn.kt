@file:OptIn(ExperimentalFoundationApi::class)

package movie.metropolis.app.screen2.listing.component

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
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.PreviewLayout
import movie.style.modifier.LightSource
import movie.style.modifier.glow
import movie.style.modifier.surface
import movie.style.shape.CompositeShape
import movie.style.shape.CutoutShape
import movie.style.theme.Theme

@Composable
fun PromotionColumn(
    color: Color,
    contentColor: Color,
    name: @Composable () -> Unit,
    rating: @Composable () -> Unit,
    poster: @Composable () -> Unit,
    action: @Composable () -> Unit,
    onClick: () -> Unit,
    onActionClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    var ratingSize by remember { mutableStateOf(DpSize.Zero) }
    var nameSize by remember { mutableStateOf(DpSize.Zero) }
    val favoriteSize = DpSize(36.dp, 36.dp)
    val baseline = Theme.container.poster
    val cornerSize = baseline.topStart
    val shape = CompositeShape(ratingSize, nameSize) {
        setBaseline(baseline)
        addShape(
            shape = CutoutShape(cornerSize, CutoutShape.Orientation.TopRight),
            size = ratingSize,
            alignment = Alignment.TopEnd,
            operation = PathOperation.Difference
        )
        addShape(
            shape = CutoutShape(cornerSize, CutoutShape.Orientation.TopLeft),
            size = favoriteSize,
            alignment = Alignment.TopStart,
            operation = PathOperation.Difference
        )
        addShape(
            shape = CutoutShape(cornerSize, CutoutShape.Orientation.BottomLeft),
            size = nameSize,
            alignment = Alignment.BottomStart,
            operation = PathOperation.Difference
        )
    }
    val containerColor = Theme.color.container.background
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .surface(containerColor, shape, 0.dp, color)
                .combinedClickable(onClick = onClick, onLongClick = onLongClick, role = Role.Image)
                .glow(shape, color),
            propagateMinConstraints = true
        ) {
            poster()
        }
        Box(
            modifier = Modifier
                .onSizeChanged {
                    nameSize = with(density) { DpSize(it.width.toDp(), it.height.toDp()) }
                }
                .padding(top = 4.dp, end = 4.dp)
                .align(Alignment.BottomStart)
                .surface(color, CircleShape, 0.dp, color)
                .glow(CircleShape, color, lightSource = LightSource.Top)
                .padding(8.dp, 4.dp)
        ) {
            ProvideTextStyle(Theme.textStyle.caption.copy(fontWeight = FontWeight.Medium)) {
                CompositionLocalProvider(LocalContentColor provides contentColor) {
                    name()
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .onSizeChanged {
                    ratingSize = with(density) { DpSize(it.width.toDp(), it.height.toDp()) }
                }
        ) {
            rating()
        }
        Box(
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.TopStart)
                .surface(color, CircleShape, 0.dp, color)
                .clickable(onClick = onActionClick, role = Role.Button)
                .glow(CircleShape, contentColor, lightSource = LightSource.BottomRight)
                .padding(6.dp),
            propagateMinConstraints = true
        ) {
            action()
        }
    }
}

@Preview
@Composable
private fun PromotionColumnPreview(
    @PreviewParameter(PromotionColumnParameter::class)
    parameter: PromotionColumnParameter.Data
) = PreviewLayout {
    PromotionColumn(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.5f),
        color = Color.Green,
        contentColor = Color.Red,
        poster = { Box(Modifier.background(Color.Green)) },
        action = { Icon(Icons.Rounded.Favorite, null) },
        name = { Text("Movie name") },
        rating = {
            RatingBox(
                color = Color.Green,
                contentColor = Color.Black,
                rating = { Text("86%") },
                offset = PaddingValues(start = 4.dp, bottom = 4.dp)
            )
        },
        onClick = {},
        onActionClick = {},
        onLongClick = {},
    )
}

private class PromotionColumnParameter : PreviewParameterProvider<PromotionColumnParameter.Data> {
    override val values = sequence { yield(Data()) }

    class Data
}