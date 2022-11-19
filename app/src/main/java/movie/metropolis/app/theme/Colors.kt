package movie.metropolis.app.theme

import android.content.Context
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightPrimary = Color(0xFF9f2c93)
private val LightOnPrimary = Color(0xFFffffff)
private val LightPrimaryContainer = Color(0xFFffd6f4)
private val LightOnPrimaryContainer = Color(0xFF390036)
private val LightSecondary = Color(0xFFa600b2)
private val LightOnSecondary = Color(0xFFffffff)
private val LightSecondaryContainer = Color(0xFFffd6fa)
private val LightOnSecondaryContainer = Color(0xFF37003c)
private val LightTertiary = Color(0xFF00696f)
private val LightOnTertiary = Color(0xFFffffff)
private val LightTertiaryContainer = Color(0xFF65f6ff)
private val LightOnTertiaryContainer = Color(0xFF002022)
private val LightError = Color(0xFFba1b1b)
private val LightErrorContainer = Color(0xFFffdad4)
private val LightOnError = Color(0xFFffffff)
private val LightOnErrorContainer = Color(0xFF410001)
private val LightBackground = Color(0xFFfcfcfc)
private val LightOnBackground = Color(0xFF1f1a1d)
private val LightSurface = Color(0xFFfcfcfc)
private val LightOnSurface = Color(0xFF1f1a1d)
private val LightSurfaceVariant = Color(0xFFefdee7)
private val LightOnSurfaceVariant = Color(0xFF4f444b)
private val LightOutline = Color(0xFF80747b)
private val LightInverseOnSurface = Color(0xFFf8eef2)
private val LightInverseSurface = Color(0xFF342f32)

private val DarkPrimary = Color(0xFFffabee)
private val DarkOnPrimary = Color(0xFF5d0058)
private val DarkPrimaryContainer = Color(0xFF810778)
private val DarkOnPrimaryContainer = Color(0xFFffd6f4)
private val DarkSecondary = Color(0xFFffa9fc)
private val DarkOnSecondary = Color(0xFF5a0061)
private val DarkSecondaryContainer = Color(0xFF7f0088)
private val DarkOnSecondaryContainer = Color(0xFFffd6fa)
private val DarkTertiary = Color(0xFF38dae4)
private val DarkOnTertiary = Color(0xFF00363a)
private val DarkTertiaryContainer = Color(0xFF004f54)
private val DarkOnTertiaryContainer = Color(0xFF65f6ff)
private val DarkError = Color(0xFFffb4a9)
private val DarkErrorContainer = Color(0xFF930006)
private val DarkOnError = Color(0xFF680003)
private val DarkOnErrorContainer = Color(0xFFffdad4)
private val DarkBackground = Color(0xFF1f1a1d)
private val DarkOnBackground = Color(0xFFe9e0e3)
private val DarkSurface = Color(0xFF1f1a1d)
private val DarkOnSurface = Color(0xFFe9e0e3)
private val DarkSurfaceVariant = Color(0xFF4f444b)
private val DarkOnSurfaceVariant = Color(0xFFd2c2cb)
private val DarkOutline = Color(0xFF9b8d95)
private val DarkInverseOnSurface = Color(0xFF1f1a1d)
private val DarkInverseSurface = Color(0xFFe9e0e3)

@Composable
fun themeColors(
    context: Context = LocalContext.current,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
) = when (useDarkTheme) {
    true -> when (dynamicColor) {
        true -> dynamicDarkColorScheme(context)
        false -> currencyColorsDark
    }

    false -> when (dynamicColor) {
        true -> dynamicLightColorScheme(context)
        false -> currencyColorsLight
    }
}

private val currencyColorsLight = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,
    tertiary = LightTertiary,
    onTertiary = LightOnTertiary,
    tertiaryContainer = LightTertiaryContainer,
    onTertiaryContainer = LightOnTertiaryContainer,
    error = LightError,
    errorContainer = LightErrorContainer,
    onError = LightOnError,
    onErrorContainer = LightOnErrorContainer,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline,
    inverseOnSurface = LightInverseOnSurface,
    inverseSurface = LightInverseSurface,
)

private val currencyColorsDark = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,
    error = DarkError,
    errorContainer = DarkErrorContainer,
    onError = DarkOnError,
    onErrorContainer = DarkOnErrorContainer,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,
    inverseOnSurface = DarkInverseOnSurface,
    inverseSurface = DarkInverseSurface,
)