package movie.metropolis.app.screen.booking.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.DefaultPosterAspectRatio
import movie.style.layout.PreviewLayout
import movie.style.modifier.glow
import movie.style.modifier.surface
import movie.style.theme.Theme

@Composable
fun MovieTimeContainer(
    color: Color,
    contentColor: Color,
    name: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(8.dp),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Box(
        modifier = Modifier
            .aspectRatio(DefaultPosterAspectRatio)
            .surface(0.dp, Theme.container.poster, 16.dp, color)
            .glow(Theme.container.poster, contentColor),
        propagateMinConstraints = true
    ) {
        content()
    }
    ProvideTextStyle(
        Theme.textStyle.caption.copy(
            textAlign = TextAlign.Center,
            fontSize = 10.sp,
            lineHeight = 10.sp
        )
    ) {
        name()
    }
}

@Preview
@Composable
private fun MovieTimeContainerPreview() = PreviewLayout {
    MovieTimeContainer(
        modifier = Modifier.width(100.dp),
        color = Color.Black,
        contentColor = Color.White,
        name = { Text("Foo bar") }
    ) {
        Box(Modifier.background(Color.Blue))
    }
}