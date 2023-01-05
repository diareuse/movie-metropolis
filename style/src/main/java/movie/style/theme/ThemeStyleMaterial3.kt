package movie.style.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

internal class ThemeStyleMaterial3(
    private val scheme: ColorScheme
) : Theme.Style {

    override val container: Theme.Style.Scheme = ContainerScheme()
    override val content: Theme.Style.Scheme = ContentScheme()

    private inner class ContainerScheme : Theme.Style.Scheme {
        override val primary: Color
            get() = scheme.primaryContainer
        override val secondary: Color
            get() = scheme.secondaryContainer
        override val tertiary: Color
            get() = scheme.tertiaryContainer
        override val error: Color
            get() = scheme.errorContainer
        override val surface: Color
            get() = scheme.surfaceVariant
        override val background: Color
            get() = scheme.background
        override val outline: Color
            get() = scheme.outlineVariant
    }

    private inner class ContentScheme : Theme.Style.Scheme {
        override val primary: Color
            get() = scheme.onPrimaryContainer
        override val secondary: Color
            get() = scheme.onSecondaryContainer
        override val tertiary: Color
            get() = scheme.onTertiaryContainer
        override val error: Color
            get() = scheme.onErrorContainer
        override val surface: Color
            get() = scheme.onSurface
        override val background: Color
            get() = scheme.onBackground
        override val outline: Color
            get() = scheme.outline
    }

}