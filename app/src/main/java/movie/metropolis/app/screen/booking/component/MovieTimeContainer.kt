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
import movie.metropolis.app.screen.listing.component.contentColor
import movie.style.layout.DefaultPosterAspectRatio
import movie.style.layout.PreviewLayout
import movie.style.modifier.surface
import movie.style.theme.Theme

@Composable
fun MovieTimeContainer(
    color: Color,
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
            .surface(0.dp, Theme.container.poster, 16.dp, color),
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
        CompositionLocalProvider(
            LocalContentColor provides LocalContentColor.current.copy(.5f)
                .compositeOver(color.contentColor)
        ) {
            name()
        }
    }
}

@Preview
@Composable
private fun MovieTimeContainerPreview() = PreviewLayout {
    MovieTimeContainer(
        color = Color.Black,
        name = { Text("Foo bar") },
        modifier = Modifier.width(100.dp)
    ) {
        Box(Modifier.background(Color.Blue))
    }
}