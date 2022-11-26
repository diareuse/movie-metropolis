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

private val LightPrimary = Color(0xFF9B4600)
private val LightOnPrimary = Color(0xFFFFFFFF)
private val LightPrimaryContainer = Color(0xFFFFDBC9)
private val LightOnPrimaryContainer = Color(0xFF331200)
private val LightSecondary = Color(0xFFAC303D)
private val LightOnSecondary = Color(0xFFFFFFFF)
private val LightSecondaryContainer = Color(0xFFFFDAD9)
private val LightOnSecondaryContainer = Color(0xFF40000A)
private val LightTertiary = Color(0xFF4B52BD)
private val LightOnTertiary = Color(0xFFFFFFFF)
private val LightTertiaryContainer = Color(0xFFE0E0FF)
private val LightOnTertiaryContainer = Color(0xFF00016E)
private val LightError = Color(0xFFBA1A1A)
private val LightErrorContainer = Color(0xFFFFDAD6)
private val LightOnError = Color(0xFFFFFFFF)
private val LightOnErrorContainer = Color(0xFF410002)
private val LightBackground = Color(0xFFFFFBFF)
private val LightOnBackground = Color(0xFF201A17)
private val LightSurface = Color(0xFFFFFBFF)
private val LightOnSurface = Color(0xFF201A17)
private val LightSurfaceVariant = Color(0xFFF4DED4)
private val LightOnSurfaceVariant = Color(0xFF52443C)
private val LightOutline = Color(0xFF85746B)
private val LightInverseOnSurface = Color(0xFFFBEEE9)
private val LightInverseSurface = Color(0xFF362F2C)
private val LightInversePrimary = Color(0xFFFFB68D)
private val LightSurfaceTint = Color(0xFF9B4600)
private val LightOutlineVariant = Color(0xFFD7C2B8)
private val LightScrim = Color(0xFF000000)

private val DarkPrimary = Color(0xFFFFB68D)
private val DarkOnPrimary = Color(0xFF532200)
private val DarkPrimaryContainer = Color(0xFF763300)
private val DarkOnPrimaryContainer = Color(0xFFFFDBC9)
private val DarkSecondary = Color(0xFFFFB3B4)
private val DarkOnSecondary = Color(0xFF680016)
private val DarkSecondaryContainer = Color(0xFF8B1628)
private val DarkOnSecondaryContainer = Color(0xFFFFDAD9)
private val DarkTertiary = Color(0xFFBEC2FF)
private val DarkOnTertiary = Color(0xFF171C8E)
private val DarkTertiaryContainer = Color(0xFF3238A4)
private val DarkOnTertiaryContainer = Color(0xFFE0E0FF)
private val DarkError = Color(0xFFFFB4AB)
private val DarkErrorContainer = Color(0xFF93000A)
private val DarkOnError = Color(0xFF690005)
private val DarkOnErrorContainer = Color(0xFFFFDAD6)
private val DarkBackground = Color(0xFF201A17)
private val DarkOnBackground = Color(0xFFECE0DB)
private val DarkSurface = Color(0xFF201A17)
private val DarkOnSurface = Color(0xFFECE0DB)
private val DarkSurfaceVariant = Color(0xFF52443C)
private val DarkOnSurfaceVariant = Color(0xFFD7C2B8)
private val DarkOutline = Color(0xFF9F8D84)
private val DarkInverseOnSurface = Color(0xFF201A17)
private val DarkInverseSurface = Color(0xFFECE0DB)
private val DarkInversePrimary = Color(0xFF9B4600)
private val DarkSurfaceTint = Color(0xFFFFB68D)
private val DarkOutlineVariant = Color(0xFF52443C)
private val DarkScrim = Color(0xFF000000)

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
    inversePrimary = LightInversePrimary,
    surfaceTint = LightSurfaceTint,
    outlineVariant = LightOutlineVariant,
    scrim = LightScrim,
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
    inversePrimary = DarkInversePrimary,
    surfaceTint = DarkSurfaceTint,
    outlineVariant = DarkOutlineVariant,
    scrim = DarkScrim,
)