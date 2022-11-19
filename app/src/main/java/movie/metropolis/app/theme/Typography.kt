package movie.metropolis.app.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import movie.metropolis.app.R

private val LexendDeca = FontFamily(
    Font(R.font.lexend_deca_black, weight = FontWeight.Black),
    Font(R.font.lexend_deca_bold, weight = FontWeight.Bold),
    Font(R.font.lexend_deca_extra_bold, weight = FontWeight.ExtraBold),
    Font(R.font.lexend_deca_extra_light, weight = FontWeight.ExtraLight),
    Font(R.font.lexend_deca_light, weight = FontWeight.Light),
    Font(R.font.lexend_deca_medium, weight = FontWeight.Medium),
    Font(R.font.lexend_deca_regular, weight = FontWeight.Normal),
    Font(R.font.lexend_deca_semi_bold, weight = FontWeight.SemiBold),
    Font(R.font.lexend_deca_thin, weight = FontWeight.Thin)
)

val ThemeTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = LexendDeca,
        fontWeight = FontWeight.W400,
        fontSize = 57.sp,
        lineHeight = 57.sp,
        letterSpacing = (-0.25).sp,
    ),
    displayMedium = TextStyle(
        fontFamily = LexendDeca,
        fontWeight = FontWeight.W400,
        fontSize = 45.sp,
        lineHeight = 45.sp,
        letterSpacing = 0.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = LexendDeca,
        fontWeight = FontWeight.W400,
        fontSize = 36.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = LexendDeca,
        fontWeight = FontWeight.W400,
        fontSize = 32.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = LexendDeca,
        fontWeight = FontWeight.W400,
        fontSize = 28.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = LexendDeca,
        fontWeight = FontWeight.W400,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = LexendDeca,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = LexendDeca,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.1.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = LexendDeca,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.1.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = LexendDeca,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.1.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = LexendDeca,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = LexendDeca,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.25.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = LexendDeca,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.4.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = LexendDeca,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.5.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = LexendDeca,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 11.sp,
        letterSpacing = 0.5.sp,
    ),
)
