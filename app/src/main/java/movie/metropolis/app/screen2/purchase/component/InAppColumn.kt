package movie.metropolis.app.screen2.purchase.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
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
import movie.style.util.toDpSize

@Composable
fun InAppColumn(
    icon: @Composable () -> Unit,
    name: @Composable () -> Unit,
    description: @Composable () -> Unit,
    price: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Theme.color.container.tertiary
) {
    Box(
        modifier = modifier
    ) {
        val baseShape = Theme.container.poster
        val cornerSize = baseShape.topStart
        val iconSize = DpSize(48.dp, 48.dp)
        val offset = DpSize(8.dp, 8.dp)
        val density = LocalDensity.current
        var buttonSize by remember { mutableStateOf(DpSize.Zero) }
        val shape = CompositeShape(buttonSize) {
            setBaseline(baseShape)
            addShape(
                shape = CutoutShape(cornerSize, CutoutShape.Orientation.TopRight),
                size = iconSize + offset,
                alignment = Alignment.TopEnd,
                operation = PathOperation.Difference
            )
            addShape(
                shape = CutoutShape(cornerSize, CutoutShape.Orientation.BottomRight),
                size = buttonSize + offset,
                alignment = Alignment.BottomEnd,
                operation = PathOperation.Difference
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .surface(2.dp, shape)
                .glow(shape)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .padding(end = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProvideTextStyle(Theme.textStyle.title) {
                name()
            }
            ProvideTextStyle(Theme.textStyle.body) {
                description()
            }
        }
        Box(
            modifier = Modifier
                .size(iconSize)
                .align(Alignment.TopEnd)
                .surface(color, baseShape)
                .glow(baseShape, lightSource = LightSource.BottomLeft)
                .padding(12.dp),
            propagateMinConstraints = true
        ) {
            icon()
        }
        Box(
            modifier = Modifier
                .onSizeChanged { buttonSize = it.toDpSize(density) }
                .align(Alignment.BottomEnd)
                .surface(color, baseShape)
                .glow(baseShape)
                .clickable(role = Role.Button, onClick = onClick)
                .padding(12.dp, 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Rounded.ShoppingCart, null)
                ProvideTextStyle(Theme.textStyle.headline) {
                    price()
                }
            }
        }
    }
}

@Preview
@Composable
private fun InAppItemPreview() = PreviewLayout(modifier = Modifier.padding(16.dp)) {
    val lorem = LoremIpsum(40).values.first()
    InAppColumn(
        modifier = Modifier.fillMaxWidth(),
        icon = { Icon(Icons.Default.LocationOn, null) },
        name = { Text("Just a drink") },
        description = { Text(lorem) },
        price = { Text("$1.22") },
        onClick = {}
    )
}