package movie.metropolis.app.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable

@Composable
fun Theme(content: @Composable () -> Unit) = MaterialTheme(
    colorScheme = themeColors(),
    typography = ThemeTypography,
    shapes = ThemeShapes
) {
    Surface {
        content()
    }
}
