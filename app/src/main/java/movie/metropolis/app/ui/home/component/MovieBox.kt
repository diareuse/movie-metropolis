package movie.metropolis.app.ui.home.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.DefaultPosterAspectRatio
import movie.style.layout.PreviewLayout
import movie.style.shape.CompositeShape
import movie.style.shape.CutoutShape
import movie.style.util.pc

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
) = MovieBoxLayout(
    modifier = modifier,
    onClick = onClick,
    shape = shape,
    rating = rating,
    poster = {
        Box(
            modifier = Modifier.aspectRatio(aspectRatio),
            propagateMinConstraints = true
        ) {
            poster()
        }
    }
)

@Composable
fun RatingBox(
    modifier: Modifier = Modifier,
    rating: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.padding(vertical = .5.pc, horizontal = 1.pc)
    ) {
        ProvideTextStyle(MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)) {
            rating()
        }
    }
}

@Composable
private fun MovieBoxLayout(
    onClick: () -> Unit,
    rating: @Composable () -> Unit,
    poster: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium
) {
    var size by remember { mutableStateOf(DpSize.Zero) }
    Layout(
        modifier = modifier,
        content = {
            Box { rating() }
            Surface(
                onClick = onClick,
                shape = CompositeShape(size) {
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
    ) { (rating, poster), c ->
        val ratingP = rating.measure(c.copy(minWidth = 0, minHeight = 0))
        val posterP = poster.measure(c.copy(minWidth = 0, minHeight = 0))
        size = DpSize(ratingP.width.toDp(), ratingP.height.toDp())
        layout(posterP.width, posterP.height) {
            ratingP.place(0, 0)
            posterP.place(0, 0)
        }
    }
}

@Preview
@Composable
private fun MovieBoxPreview() = PreviewLayout {
    MovieBox(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .width(150.dp),
        onClick = {},
        name = { Text("Captain America") },
        poster = { Box(modifier = Modifier.background(Color.Green)) },
        rating = { RatingBox { Text("82%") } },
        category = { Text("Action/Adventure") }
    )
}