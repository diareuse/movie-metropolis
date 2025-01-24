package movie.metropolis.app.ui.home.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.DefaultPosterAspectRatio
import movie.style.layout.PreviewLayout
import movie.style.shape.CompositeShape
import movie.style.shape.CutoutShape
import movie.style.util.pc
import movie.style.util.toDpSize

@Composable
fun MovieBox(
    onClick: () -> Unit,
    name: @Composable () -> Unit,
    poster: @Composable () -> Unit,
    rating: @Composable () -> Unit,
    category: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    aspectRatio: Float = DefaultPosterAspectRatio,
    shape: Shape = MaterialTheme.shapes.medium
) = Box(modifier = modifier) {
    var size by remember { mutableStateOf(DpSize.Zero) }
    val density = LocalDensity.current
    Box(modifier = Modifier
        .onSizeChanged { size = it.toDpSize(density) }
        .padding(bottom = .5.pc, end = 1.pc)) {
        ProvideTextStyle(MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)) {
            rating()
        }
    }
    Surface(
        modifier = Modifier
            .aspectRatio(aspectRatio),
        onClick = onClick,
        shape = CompositeShape(shape, size) {
            setBaseline(shape)
            val cs = if (shape is CornerBasedShape) shape.topStart else CornerSize(1.pc)
            addShape(
                shape = CutoutShape(cs, CutoutShape.Orientation.TopLeft),
                size = size,
                alignment = Alignment.TopStart,
                operation = PathOperation.Difference
            )
        }
    ) {
        poster()
    }
}

@PreviewLightDark
@PreviewFontScale
@Composable
private fun MovieBoxPreview() = PreviewLayout {
    MovieBox(
        modifier = Modifier.width(150.dp),
        onClick = {},
        name = { Text("Captain America") },
        poster = { Box(modifier = Modifier.background(Color.Green)) },
        rating = { Text("82%") },
        category = { Text("Action/Adventure") }
    )
}