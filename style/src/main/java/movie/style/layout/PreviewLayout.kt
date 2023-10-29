@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package movie.style.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material3.windowsizeclass.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import movie.style.LocalWindowSizeClass
import movie.style.theme.Theme

@Composable
fun PreviewLayout(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(0.dp),
    content: @Composable () -> Unit
) = Theme {
    BoxWithConstraints(modifier = modifier.padding(padding)) {
        val size = DpSize(maxWidth, maxHeight)
        CompositionLocalProvider(
            LocalWindowSizeClass provides WindowSizeClass.calculateFromSize(size)
        ) {
            content()
        }
    }
}

@Composable
fun PreviewWearLayout(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(16.dp),
    content: @Composable () -> Unit
) = Theme(darkTheme = true) {
    Box(
        modifier = modifier.padding(padding),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}