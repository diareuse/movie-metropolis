package ui.style.theme

import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.*
import ui.style.haptic.PlatformHapticFeedback
import ui.style.haptic.rememberVibrateIndication

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
        LocalHapticFeedback provides PlatformHapticFeedback(LocalView.current),
        LocalIndication provides rememberVibrateIndication()
    ) {
        Surface {
            content()
        }
    }
}