package movie.metropolis.app.ui.movie

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.DefaultPosterAspectRatio
import movie.style.layout.PreviewLayout

@Composable
fun BackdropLayout(
    backdrop: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    offset: Dp = 64.dp,
    content: @Composable (PaddingValues) -> Unit,
) {
    var contentPadding by remember { mutableStateOf(PaddingValues()) }
    Layout(
        modifier = modifier,
        content = {
            backdrop()
            content(contentPadding)
        }
    ) { (bg, ct), b ->
        val backdrop = bg.measure(b)
        contentPadding = PaddingValues(top = (backdrop.height.toDp() - offset).coerceAtLeast(0.dp))
        val content = ct.measure(b)
        val width = maxOf(backdrop.width, content.width)
        val height = maxOf(backdrop.height, content.height)
        layout(width, height) {
            backdrop.place(0, 0)
            content.place(0, 0)
        }
    }
}

@Preview
@Composable
private fun BackdropLayoutPreview() = PreviewLayout {
    BackdropLayout(backdrop = {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Blue)
                .aspectRatio(DefaultPosterAspectRatio)
        )
    }) {
        Box(
            Modifier
                .padding(it)
                .fillMaxSize()
                .background(Color.Green)
        )
    }
}