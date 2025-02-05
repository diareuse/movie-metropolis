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
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import movie.style.layout.DefaultPosterAspectRatio
import movie.style.layout.PreviewLayout
import movie.style.util.pc

@Composable
fun MovieBox(
    haze: HazeState,
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
            modifier = Modifier
                .aspectRatio(aspectRatio)
                .hazeSource(haze),
            propagateMinConstraints = true
        ) {
            poster()
        }
    },
    name = name,
    category = category
)

@Composable
fun RatingBox(
    color: Color,
    haze: HazeState,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    alpha: Float = .4f,
    rating: @Composable () -> Unit,
) = Box(
    modifier = modifier
        .padding(.5.pc)
        .clip(shape)
        .hazeEffect(
            state = haze, style = HazeStyle(
                backgroundColor = MaterialTheme.colorScheme.surface,
                tint = HazeTint(color.copy(.25f)),
                blurRadius = 4.dp,
                noiseFactor = 7f
            )
        )
        .shadow(8.dp, shape = shape, clip = false, spotColor = color, ambientColor = color)
        .border(Dp.Hairline, color.copy(alpha), shape)
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
    name: @Composable () -> Unit,
    category: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium
) {
    Column(modifier = modifier) {
        Box(contentAlignment = Alignment.TopCenter) {
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
        Column(modifier = Modifier.padding(vertical = .5.pc, horizontal = .5.pc)) {
            ProvideTextStyle(MaterialTheme.typography.labelSmall) {
                name()
                Box(modifier = Modifier.alpha(.5f)) {
                    category()
                }
            }
        }
    }
}

@Preview
@Composable
private fun MovieBoxPreview() = PreviewLayout {
    val haze = remember { HazeState() }
    MovieBox(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(1.pc)
            .width(10.pc),
        haze = haze,
        onClick = {},
        name = { Text("Captain America") },
        poster = { Box(modifier = Modifier.background(Color.Green)) },
        rating = { RatingBox(Color.Green, haze) { Text("82%") } },
        category = { Text("Action/Adventure") })
}