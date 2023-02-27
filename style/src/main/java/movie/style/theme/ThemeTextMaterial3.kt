package movie.style.theme

import androidx.compose.material3.*
import androidx.compose.ui.text.*

internal class ThemeTextMaterial3(
    private val typography: Typography
) : Theme.Text {

    override val caption: TextStyle
        get() = typography.bodySmall
    override val body: TextStyle
        get() = typography.bodyMedium
    override val emphasis: TextStyle
        get() = typography.bodyLarge
    override val title: TextStyle
        get() = typography.titleLarge
    override val headline: TextStyle
        get() = typography.titleMedium

}