package movie.metropolis.app.screen2.cinema.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.rounded.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import androidx.compose.ui.unit.*
import movie.style.layout.PreviewLayout
import movie.style.modifier.LightSource
import movie.style.modifier.glow
import movie.style.modifier.surface
import movie.style.shape.CompositeShape
import movie.style.shape.CutoutShape
import movie.style.theme.Theme

@Composable
fun PermissionBox(
    icon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    message: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val iconSize = DpSize(48.dp, 48.dp)
    val baselineShape = Theme.container.poster
    val shape = CompositeShape {
        setBaseline(baselineShape)
        addShape(
            shape = CutoutShape(CornerSize(24.dp), CutoutShape.Orientation.TopRight),
            size = iconSize,
            alignment = Alignment.TopEnd,
            operation = PathOperation.Difference
        )
    }
    Box(modifier = modifier.wrapContentHeight()) {
        Box(
            modifier = Modifier
                .size(iconSize)
                .padding(start = 8.dp, bottom = 8.dp)
                .align(Alignment.TopEnd)
                .surface(
                    Theme.color.container.error,
                    CircleShape,
                    16.dp,
                    Theme.color.emphasis.error
                )
                .clickable(onClick = onClick, role = Role.Button)
                .glow(
                    CircleShape,
                    lightSource = LightSource.BottomLeft,
                    color = Theme.color.content.error
                )
                .padding(8.dp),
            propagateMinConstraints = true
        ) {
            Icon(Icons.AutoMirrored.Rounded.ArrowForward, null)
        }
        Row(
            modifier = Modifier
                .surface(Theme.color.container.error, shape, 16.dp, Theme.color.emphasis.error)
                .clickable(onClick = onClick, role = Role.Button)
                .glow(shape)
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProvideTextStyle(Theme.textStyle.title) {
                    title()
                }
                ProvideTextStyle(Theme.textStyle.caption) {
                    message()
                }
            }
            icon()
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun PermissionBoxPreview() = PreviewLayout(modifier = Modifier.padding(16.dp)) {
    val lorem = LoremIpsum(25).values.first()
    PermissionBox(
        icon = { Icon(Icons.Rounded.LocationOn, null) },
        title = { Text("Permission") },
        message = { Text(lorem) },
        onClick = {}
    )
}