package movie.style.theme

import androidx.compose.material3.*
import androidx.compose.ui.graphics.*

internal class ThemeStyleMaterial3(
    private val scheme: ColorScheme
) : Theme.Style {

    override val container: Theme.Style.Scheme = ContainerScheme()
    override val content: Theme.Style.Scheme = ContentScheme()
    override val emphasis: Theme.Style.Scheme = EmphasisScheme()

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

    private inner class EmphasisScheme : Theme.Style.Scheme {
        override val primary: Color
            get() = scheme.primary
        override val secondary: Color
            get() = scheme.secondary
        override val tertiary: Color
            get() = scheme.tertiary
        override val error: Color
            get() = scheme.error
        override val surface: Color
            get() = scheme.surface
        override val background: Color
            get() = scheme.background
        override val outline: Color
            get() = scheme.outline
    }

}