package movie.style

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.PreviewLayout

@Composable
fun TwoPaneSurface(
    primary: @Composable () -> Unit,
    secondary: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    separator: @Composable () -> Unit = { TwoPaneSurfaceDefaults.Separator() },
    sizeClass: WindowWidthSizeClass = LocalWindowSizeClass.current.widthSizeClass
) = when (sizeClass) {
    WindowWidthSizeClass.Expanded -> TwoPaneSurface(
        primary = primary,
        secondary = secondary,
        separator = separator,
        modifier = modifier
    )

    else -> SinglePaneSurface(
        primary = primary,
        modifier = modifier
    )
}

@Composable
private fun TwoPaneSurface(
    primary: @Composable () -> Unit,
    secondary: @Composable () -> Unit,
    separator: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) = Row(modifier = modifier) {
    Box(modifier = Modifier.weight(1f), propagateMinConstraints = true) {
        primary()
    }
    separator()
    Box(modifier = Modifier.weight(1f), propagateMinConstraints = true) {
        secondary()
    }
}

@Composable
private fun SinglePaneSurface(
    primary: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) = Box(modifier = modifier, propagateMinConstraints = true) {
    primary()
}

object TwoPaneSurfaceDefaults {

    @Composable
    fun Separator(
        modifier: Modifier = Modifier,
        color: Color = LocalContentColor.current.copy(alpha = .18f),
        width: Dp = 1.dp,
        shape: Shape = CircleShape
    ) {
        Box(
            modifier
                .fillMaxHeight()
                .width(width)
                .statusBarsPadding()
                .navigationBarsPadding()
                .background(color, shape)
        )
    }

}

@Preview(showBackground = true)
@Preview(showBackground = true, widthDp = 1000)
@Composable
private fun TwoPaneSurfacePreview() = PreviewLayout {
    TwoPaneSurface({ Text("A") }, { Text("B") })
}