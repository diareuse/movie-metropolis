package movie.metropolis.app.ui.home.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
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
) = Surface(
    modifier = modifier,
    onClick = onClick,
    shape = MaterialTheme.shapes.medium,
    tonalElevation = 1.pc
) {
    Column(modifier = Modifier.padding(1.pc)) {
        Box(
            modifier = Modifier
                .aspectRatio(aspectRatio)
                .clip(MaterialTheme.shapes.small),
            propagateMinConstraints = true
        ) {
            poster()
        }
        Spacer(Modifier.height(1.pc))
        name()
        category()
        rating()
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