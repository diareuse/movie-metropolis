package movie.style.theme

import androidx.compose.foundation.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.*
import movie.style.haptic.PlatformHapticFeedback
import movie.style.haptic.rememberVibrateIndication

@Composable
fun Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) = MaterialTheme(
    colorScheme = themeColors(useDarkTheme = darkTheme),
    typography = ThemeTypography,
    shapes = ThemeShapes
) {
    CompositionLocalProvider(
        LocalText provides ThemeTextMaterial3(MaterialTheme.typography),
        LocalContainer provides ThemeContainerMaterial3(MaterialTheme.shapes),
        LocalStyle provides ThemeStyleMaterial3(MaterialTheme.colorScheme),
        LocalHapticFeedback provides PlatformHapticFeedback(LocalView.current),
        LocalIndication provides rememberVibrateIndication()
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides Theme.textStyle.body,
            LocalContentColor provides Theme.color.content.background
        ) {
            content()
        }
    }
}

private val LocalText =
    staticCompositionLocalOf<Theme.Text> { error("Not provided") }

private val LocalContainer =
    staticCompositionLocalOf<Theme.Container> { error("Not provided") }

private val LocalStyle =
    staticCompositionLocalOf<Theme.Style> { error("Not provided") }

object Theme {

    val textStyle: Text @Composable get() = LocalText.current

    val container: Container @Composable get() = LocalContainer.current

    val color: Style @Composable get() = LocalStyle.current

    @Immutable
    interface Text {
        val caption: TextStyle
        val body: TextStyle
        val emphasis: TextStyle
        val title: TextStyle
        val headline: TextStyle
    }

    @Immutable
    interface Container {
        val button: CornerBasedShape
        val poster: CornerBasedShape get() = button
        val buttonSmall: CornerBasedShape
        val card: CornerBasedShape
    }

    @Immutable
    interface Style {

        val container: Scheme
        val content: Scheme
        val emphasis: Scheme

        @Immutable
        interface Scheme {
            val primary: Color
            val secondary: Color
            val tertiary: Color
            val error: Color
            val surface: Color
            val background: Color
            val outline: Color
        }

    }

}

fun Theme.Style.contentColorFor(color: Color) = when (color) {
    container.primary -> content.primary
    container.secondary -> content.secondary
    container.tertiary -> content.tertiary
    container.error -> content.error
    container.surface -> content.surface
    container.background -> content.background
    container.outline -> content.outline
    content.primary -> container.primary
    content.secondary -> container.secondary
    content.tertiary -> container.tertiary
    content.error -> container.error
    content.surface -> container.surface
    content.background -> container.background
    content.outline -> container.outline
    else -> if (color.luminance() < .5f) Color.White else Color.Black
}