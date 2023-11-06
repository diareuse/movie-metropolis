@file:OptIn(ExperimentalLayoutApi::class)

package movie.metropolis.app.screen2.purchase.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.R
import movie.metropolis.app.model.ProjectionType
import movie.metropolis.app.model.ShowingTag
import movie.style.layout.PreviewLayout
import movie.style.modifier.glow
import movie.style.modifier.surface
import movie.style.theme.Theme
import java.util.Locale

@Composable
fun ProjectionTypeRow(
    language: @Composable () -> Unit,
    subtitle: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    projections: @Composable () -> Unit,
) = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(8.dp),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        language()
        subtitle()
    }
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        projections()
    }
}

@Preview(showBackground = true)
@Composable
private fun ProjectionTypeRowPreview(
    @PreviewParameter(ShowingTagParameter::class)
    parameter: ShowingTag
) = PreviewLayout(modifier = Modifier.padding(16.dp)) {
    val locale = Locale.getDefault()
    ProjectionTypeRow(
        language = {
            ProjectionTypeRowDefaults.Speech {
                Text(parameter.language.getDisplayLanguage(locale))
            }
        },
        subtitle = {
            if (parameter.subtitles != null) ProjectionTypeRowDefaults.Subtitle {
                Text(parameter.subtitles.getDisplayLanguage(locale))
            }
        }
    ) {
        for (p in parameter.projection) when (p) {
            ProjectionType.Imax -> ProjectionTypeRowDefaults.TypeImax()
            ProjectionType.Plane2D -> ProjectionTypeRowDefaults.Type2D()
            ProjectionType.Plane3D -> ProjectionTypeRowDefaults.Type3D()
            ProjectionType.Plane4DX -> ProjectionTypeRowDefaults.Type4DX()
            ProjectionType.DolbyAtmos -> ProjectionTypeRowDefaults.DolbyAtmos()
            ProjectionType.HighFrameRate -> ProjectionTypeRowDefaults.HighFrameRate()
            ProjectionType.VIP -> ProjectionTypeRowDefaults.VIP()
            is ProjectionType.Other -> ProjectionTypeRowDefaults.TypeOther(p.type)
        }
    }
}

class ShowingTagParameter : PreviewParameterProvider<ShowingTag> {
    override val values = sequence {
        val lang = Locale.FRENCH
        val sub = Locale.ENGLISH
        yield(ShowingTag(lang, sub, listOf(ProjectionType.Imax, ProjectionType.Plane2D)))
        yield(ShowingTag(lang, sub, listOf(ProjectionType.Plane3D, ProjectionType.Plane4DX)))
        yield(
            ShowingTag(
                lang,
                sub,
                listOf(ProjectionType.DolbyAtmos, ProjectionType.HighFrameRate)
            )
        )
        yield(
            ShowingTag(
                lang,
                sub,
                listOf(ProjectionType.VIP, ProjectionType.Other("Blowjob & Fun"))
            )
        )
    }
}

object ProjectionTypeRowDefaults {

    @Composable
    fun Speech(
        modifier: Modifier = Modifier,
        label: @Composable () -> Unit
    ) = IconifiedProjectionType(
        modifier = modifier,
        icon = { Icon(painterResource(id = R.drawable.ic_speech), null) },
        label = label
    )

    @Composable
    fun Subtitle(
        modifier: Modifier = Modifier,
        label: @Composable () -> Unit,
    ) = IconifiedProjectionType(
        modifier = modifier,
        icon = { Icon(painterResource(id = R.drawable.ic_subtitles), null) },
        label = label
    )

    @Composable
    fun TypeImax(
        modifier: Modifier = Modifier,
        shape: Shape = RoundedCornerShape(4.dp)
    ) = ProjectionTypeBadge(
        modifier = modifier.drawBehind {
            val outline = shape.createOutline(size, layoutDirection, this)
            val offset = 3.dp.toPx()
            scale(1.2f, 1.4f) {
                drawOutline(outline, Color(0x80377CCB))
            }
        },
        color = Color(0xFF377CCB),
        contentColor = Color.White,
        label = { Text("IMAX") },
        emphasize = true
    )

    @Composable
    fun Type2D(
        modifier: Modifier = Modifier,
    ) = ProjectionTypeBadge(
        modifier = modifier,
        color = Color(0xFFB9F6CA),
        contentColor = Color.Black,
        label = { Text("2D") },
        emphasize = true
    )

    @Composable
    fun Type3D(
        modifier: Modifier = Modifier,
        shape: Shape = RoundedCornerShape(4.dp)
    ) = ProjectionTypeBadge(
        modifier = modifier.drawBehind {
            val outline = shape.createOutline(size, layoutDirection, this)
            val offset = 3.dp.toPx()
            translate(left = -offset, top = -offset) {
                drawOutline(outline, Color.Red)
            }
            translate(left = offset, top = offset) {
                drawOutline(outline, Color.Blue)
            }
        },
        shape = shape,
        color = Color(0xFF69F0AE),
        contentColor = Color.Black,
        label = { Text("3D") },
        emphasize = true
    )

    @Composable
    fun Type4DX(
        modifier: Modifier = Modifier,
        shape: Shape = RoundedCornerShape(4.dp)
    ) = ProjectionTypeBadge(
        modifier = modifier.drawBehind {
            val outline = shape.createOutline(size, layoutDirection, this)
            val rotation = 20f
            rotate(rotation) {
                drawOutline(outline, Color(0x80FF6E40))
            }
            rotate(-rotation) {
                drawOutline(outline, Color(0x80FF6E40))
            }
        },
        shape = shape,
        color = Color(0xFFFF6E40),
        contentColor = Color.Black,
        label = { Text("4DX") },
        emphasize = true
    )

    @Composable
    fun DolbyAtmos(
        modifier: Modifier = Modifier,
        shape: RoundedCornerShape = RoundedCornerShape(4.dp)
    ) = ProjectionTypeBadge(
        modifier = modifier.drawBehind {
            val offset = 2.dp.toPx()
            val outline = shape.copy(CornerSize(6.dp)).createOutline(size, layoutDirection, this)
            val colors = listOf(Color(0xFF3D4FF2), Color.Transparent)
            val end = Offset(size.width / 2, size.height / 2)
            val style = Stroke(2.dp.toPx())
            translate(left = -offset, top = -offset) {
                drawOutline(outline, Brush.linearGradient(colors, end = end), style = style)
            }
            translate(left = offset, top = offset) {
                drawOutline(
                    outline,
                    Brush.linearGradient(colors, start = Offset.Infinite, end = end),
                    style = style
                )
            }
        },
        color = Color(0xFF3D4FF2),
        contentColor = Color.White,
        label = {
            Icon(
                modifier = Modifier.padding(0.dp, 2.dp),
                painter = painterResource(id = R.drawable.ic_dolby),
                contentDescription = null
            )
        },
        emphasize = true
    )

    @Composable
    fun HighFrameRate(
        modifier: Modifier = Modifier,
        shape: Shape = RoundedCornerShape(4.dp)
    ) = ProjectionTypeBadge(
        modifier = modifier.drawBehind {
            val outline = shape.createOutline(size, layoutDirection, this)
            val offset = 2.dp.toPx()
            translate(offset, -offset) {
                drawOutline(outline, Color(0x803D4FF2))
            }
            translate(offset * 2, -offset * 2) {
                drawOutline(outline, Color(0x803D4FF2))
            }
        },
        color = Color(0xFF3D4FF2),
        contentColor = Color.White,
        label = { Text("HFR") },
        emphasize = true
    )

    @Composable
    fun VIP(
        modifier: Modifier = Modifier
    ) = ProjectionTypeBadge(
        modifier = modifier.drawBehind {
            val crownSize = Size(14.dp.toPx(), 6.dp.toPx())
            val path = Path().apply {
                moveTo(size.width / 2 - crownSize.width / 2, 0f)
                relativeLineTo(0f, -crownSize.height)
                relativeLineTo(crownSize.width / 2, crownSize.height / 2)
                relativeLineTo(crownSize.width / 2, -crownSize.height / 2)
                relativeLineTo(0f, crownSize.height)
            }
            drawPath(path, Color(0xffffd700))
            drawPath(
                path,
                Color.Black.copy(alpha = .3f),
                style = Stroke(1.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Bevel)
            )
        },
        color = Color(0xffffd700),
        contentColor = Color.Black,
        label = { Text("VIP") },
        emphasize = true
    )

    @Composable
    fun TypeOther(
        name: String,
        modifier: Modifier = Modifier,
    ) = ProjectionTypeBadge(
        modifier = modifier,
        color = Color(0xFFC4C4C4),
        contentColor = Color.Black,
        label = { Text(name) }
    )

}

@Composable
private fun IconifiedProjectionType(
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) = Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
    Box(modifier = Modifier.size(16.dp), propagateMinConstraints = true) {
        icon()
    }
    ProvideTextStyle(Theme.textStyle.caption) {
        label()
    }
}

@Composable
private fun ProjectionTypeBadge(
    color: Color,
    contentColor: Color,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    emphasize: Boolean = false,
    shape: Shape = RoundedCornerShape(4.dp)
) = Box(
    modifier = modifier
        .height(20.dp)
        .surface(
            color = color,
            shape = shape,
            elevation = if (emphasize) 8.dp else 0.dp,
            shadowColor = color
        )
        .glow(shape)
        .padding(4.dp, 2.dp)
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        ProvideTextStyle(Theme.textStyle.caption) {
            label()
        }
    }
}