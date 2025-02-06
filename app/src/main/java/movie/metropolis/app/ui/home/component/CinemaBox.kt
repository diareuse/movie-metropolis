package movie.metropolis.app.ui.home.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.screen.cinema.component.CinemaViewProvider
import movie.style.Image
import movie.style.layout.PreviewLayout
import movie.style.rememberImageState
import movie.style.util.pc

@Composable
fun CinemaBox(
    onClick: () -> Unit,
    name: @Composable () -> Unit,
    city: @Composable () -> Unit,
    distance: @Composable () -> Unit,
    image: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    haze: HazeState = remember { HazeState() }
) = Surface(
    modifier = modifier,
    onClick = onClick,
    shape = MaterialTheme.shapes.medium
) {
    CinemaBoxLayout(
        image = {
            Box(
                modifier = Modifier.hazeSource(haze),
                propagateMinConstraints = true
            ) {
                image()
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .hazeEffect(
                        state = haze,
                        style = HazeStyle(
                            backgroundColor = MaterialTheme.colorScheme.surface,
                            tint = HazeTint(MaterialTheme.colorScheme.surface.copy(.25f)),
                            noiseFactor = 10f,
                            blurRadius = 8.dp
                        )
                    )
                    .padding(1.pc, 1.pc),
                verticalArrangement = Arrangement.Bottom
            ) {
                ProvideTextStyle(MaterialTheme.typography.labelSmall) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(1.pc)
                    ) {
                        name()
                        distance()
                    }
                    Box(modifier = Modifier.alpha(.75f)) {
                        city()
                    }
                }
            }
        }
    )
}

@Composable
private fun CinemaBoxLayout(
    image: @Composable () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 100.dp
) = Layout({
    image()
    content()
}, modifier) { (image, content), c ->
    val imageP = image.measure(
        Constraints.fixedHeight(height.roundToPx())
            .copy(minWidth = c.minWidth, maxWidth = c.maxWidth)
    )
    val contentP = content.measure(
        Constraints.fixedWidth(imageP.width)
            .copy(minHeight = c.minHeight, maxHeight = imageP.height)
    )
    val w = imageP.width
    val h = imageP.height
    layout(w, h) {
        imageP.place(0, 0)
        contentP.place(0, h - contentP.height)
    }
}

@PreviewLightDark
@PreviewFontScale
@Composable
private fun CinemaBoxPreview(
    @PreviewParameter(CinemaViewProvider::class, limit = 1)
    view: CinemaView
) = PreviewLayout {
    CinemaBox(
        onClick = {},
        name = { Text(view.name) },
        city = { Text(view.city) },
        distance = {
            val d = view.distance
            if (d != null) Text(d)
        },
        image = { Image(rememberImageState(view.image)) }
    )
}