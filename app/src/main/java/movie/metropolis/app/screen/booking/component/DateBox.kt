package movie.metropolis.app.screen.booking.component

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.PreviewLayout
import movie.style.modifier.glow
import movie.style.modifier.surface
import movie.style.theme.Theme

@Composable
fun DateBox(
    active: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val color by animateColorAsState(targetValue = if (active) Theme.color.container.tertiary else Theme.color.container.surface)
        val contentColor by animateColorAsState(targetValue = if (active) Theme.color.content.tertiary else Theme.color.content.surface)
        Box(
            modifier = Modifier
                .surface(color, Theme.container.button, 16.dp)
                .glow(Theme.container.button, contentColor)
                .padding(16.dp)
        ) {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                ProvideTextStyle(Theme.textStyle.title.copy(textAlign = TextAlign.Center)) {
                    content()
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun DateBoxPreview() = PreviewLayout(modifier = Modifier.padding(16.dp)) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        DateBox(active = true) {
            Text("03\nFeb")
        }
        DateBox(active = false) {
            Text("03\nFeb")
        }
    }
}