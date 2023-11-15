package movie.metropolis.app.screen.movie.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.PreviewLayout
import movie.style.layout.plus
import movie.style.modifier.LightSource
import movie.style.modifier.glow
import movie.style.modifier.surface
import movie.style.shape.CompositeShape
import movie.style.theme.Theme
import kotlin.math.max

@Composable
fun ActorRow(
    image: @Composable () -> Unit,
    name: @Composable () -> Unit,
    popularity: @Composable () -> Unit,
    movieCount: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Theme.color.container.background,
    imageOffset: PaddingValues = PaddingValues(16.dp),
    imageSize: DpSize = DpSize(64.dp, 64.dp),
    imagePadding: Dp = 8.dp,
    imageRingPadding: Dp = 1.dp,
    imageRingWidth: Dp = 2.dp
) = ActorRowLayout(
    modifier = modifier,
    picture = {
        val pictureShape = CircleShape
        Box(
            modifier = Modifier
                .size(imageSize)
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        val outlinePaddingPx = imageRingPadding.toPx()
                        val outlineWidthPx = imageRingWidth.toPx()
                        val outlineSize = size.copy(
                            width = size.width + outlinePaddingPx * 2 + outlineWidthPx * 2,
                            height = size.height + outlinePaddingPx * 2 + outlineWidthPx * 2
                        )
                        val outline = pictureShape.createOutline(outlineSize, layoutDirection, this)
                        val style = Stroke(outlineWidthPx)
                        translate(
                            left = -(outlinePaddingPx + outlineWidthPx),
                            top = -(outlinePaddingPx + outlineWidthPx)
                        ) {
                            drawOutline(outline, color.copy(.4f), style = style)
                        }
                    }
                }
                .surface(color, pictureShape, 16.dp),
            propagateMinConstraints = true
        ) {
            image()
        }
    },
    offset = imageOffset,
    background = {
        val surfaceColor = Theme.color.container.background
        val shape = CompositeShape {
            val imagePadding = imagePadding + imageRingPadding + imageRingWidth + imageRingPadding
            setBaseline(RoundedCornerShape(20.dp))
            addShape(
                shape = ActorShape(imageOffset + PaddingValues(horizontal = imagePadding / 2)),
                alignment = Alignment.CenterStart,
                size = imageSize + DpSize(imagePadding, imagePadding),
                operation = PathOperation.Difference
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .surface(surfaceColor, shape)
                .glow(shape, color, LightSource.Left, fillAlpha = .4f, alpha = .2f)
        )
    }
) {
    Column(
        modifier = Modifier
            .width(IntrinsicSize.Max)
            .padding(vertical = 12.dp)
            .padding(end = 24.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ProvideTextStyle(Theme.textStyle.headline.copy(fontWeight = FontWeight.Medium)) {
            name()
        }
        val colorVariant = Theme.color.content.background.copy(.4f)
        HorizontalDivider(modifier = Modifier.width(64.dp), color = colorVariant.copy(.1f))
        ProvideTextStyle(Theme.textStyle.caption) {
            CompositionLocalProvider(LocalContentColor provides colorVariant) {
                Row(
                    modifier = Modifier.wrapContentWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("Popularity")
                        ProvideTextStyle(LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)) {
                            CompositionLocalProvider(LocalContentColor provides colorVariant.copy(1f)) {
                                popularity()
                            }
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("No. of movies")
                        ProvideTextStyle(LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)) {
                            CompositionLocalProvider(LocalContentColor provides colorVariant.copy(1f)) {
                                movieCount()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActorRowLayout(
    picture: @Composable () -> Unit,
    background: @Composable () -> Unit,
    offset: PaddingValues,
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable () -> Unit
) = Layout(
    modifier = modifier,
    content = {
        Box { content() }
        Box { picture() }
        Box { background() }
    }
) { measurables, constraints ->
    val offsetStart = offset.calculateStartPadding(layoutDirection).roundToPx()
    val constraints = constraints.copy(minWidth = 0, minHeight = 0)
    val picture = measurables[1].measure(constraints)
    val content = measurables[0].measure(constraints)
    val background =
        measurables[2].measure(Constraints.fixed(content.width + picture.width, content.height))
    val width = content.width + offsetStart + picture.width
    val height = max(content.height, picture.height)
    layout(width, height) {
        background.placeRelative(
            x = offsetStart,
            y = verticalAlignment.align(background.height, height)
        )
        content.placeRelative(
            x = offsetStart + picture.width,
            y = verticalAlignment.align(content.height, height)
        )
        picture.placeRelative(x = 0, y = verticalAlignment.align(picture.height, height))
    }
}

class ActorShape(
    private val offset: PaddingValues
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline = with(density) {
        val path = Path()
        val offset = Offset(x = -offset.calculateStartPadding(layoutDirection).toPx(), y = 0f)
        path.addOval(Rect(offset, size))
        return Outline.Generic(path)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun ActorRowPreview() = PreviewLayout {
    ActorRow(
        image = {
            Icon(
                Icons.Rounded.Person, null,
                Modifier
                    .surface(Color.Magenta)
                    .padding(12.dp)
            )
        },
        name = { Text("Brie Larson") },
        popularity = { Text("139") },
        movieCount = { Text("4") },
        color = Color.Magenta
    )
}