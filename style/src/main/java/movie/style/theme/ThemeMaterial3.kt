package movie.style.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography

internal class ThemeMaterial3(
    private val typography: Typography,
    private val shapes: Shapes,
    private val scheme: ColorScheme
) : Theme {

    override val textStyle: Theme.Text
        get() = ThemeTextMaterial3(typography)
    override val container: Theme.Container
        get() = ThemeContainerMaterial3(shapes)
    override val color: Theme.Style
        get() = ThemeStyleMaterial3(scheme)

}