package movie.metropolis.app.ui.home.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.DefaultPosterAspectRatio
import movie.style.layout.PreviewLayout
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
    color: Color,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    alpha: Float = .4f,
    rating: @Composable () -> Unit,
) = Box(
    modifier = modifier
        .padding(.5.pc)
        .shadow(8.dp, shape = shape, clip = false, spotColor = color, ambientColor = color)
        .border(Dp.Hairline, color.copy(alpha), shape)
        .background(MaterialTheme.colorScheme.surface, shape)
        .padding(vertical = .25.pc, horizontal = .5.pc)
) {
    ProvideTextStyle(MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)) {
        rating()
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
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter,
        propagateMinConstraints = true
    ) {
        Surface(
            shape = shape,
            onClick = onClick
        ) {
            poster()
        }
        Box(modifier = Modifier.wrapContentSize()) {
            rating()
        }
    }
}

@Preview
@Composable
private fun MovieBoxPreview() = PreviewLayout {
    MovieBox(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(1.pc)
            .width(10.pc),
        onClick = {},
        name = { Text("Captain America") },
        poster = { Box(modifier = Modifier.background(Color.Green)) },
        rating = { RatingBox(Color.Green) { Text("82%") } },
        category = { Text("Action/Adventure") }
    )
}