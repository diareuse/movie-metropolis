package movie.metropolis.app.screen2.purchase.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.PreviewLayout
import movie.style.theme.Theme
import java.text.DateFormat
import java.util.Calendar
import java.util.Date
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


private const val RadiansToDegrees = (180.0 / PI).toFloat()

private fun degrees(radians: Float): Float = RadiansToDegrees * radians
private fun radians(degrees: Float): Float = degrees / RadiansToDegrees

@Composable
fun TimeButton(
    time: Long,
    modifier: Modifier = Modifier,
    dayRange: IntRange = 7..17,
    colors: TimeButtonColors = TimeButtonDefaults.colors(),
    content: @Composable () -> Unit,
) {
    val calendar = remember(time) {
        Calendar
            .getInstance()
            .apply {
                this.time = Date(time)
            }
    }
    val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
    val containerColor = if (hourOfDay in dayRange) colors.day else colors.night
    val contentColor = if (hourOfDay !in dayRange) colors.day else colors.night
    Box(
        modifier = modifier
            .clip(Theme.container.button)
            .drawBehind {
                val timeAngle = time % 86400000f / 86400000f * 360f + 270f
                // sun & moon
                val sunRadius = 16.dp.toPx() / 2
                val radius = (size.width / 2) / 1.5f
                val sunAngleOffset = 270f
                val moonAngleOffset = 90f
                val sunAngle = sunAngleOffset - timeAngle
                val moonAngle = moonAngleOffset - timeAngle
                val sunCenter =
                    Offset(radius * sin(radians(sunAngle)), radius * cos(radians(sunAngle)))
                val moonCenter =
                    Offset(radius * sin(radians(moonAngle)), radius * cos(radians(moonAngle)))
                drawRect(
                    Brush.verticalGradient(
                        listOf(
                            colors.sky
                                .copy(alpha = .5f)
                                .compositeOver(containerColor),
                            colors.sky
                                .copy(alpha = .5f)
                                .compositeOver(containerColor)
                        )
                    )
                )
                drawCircle(
                    colors.sun,
                    radius = sunRadius,
                    center = sunCenter + Offset(size.width / 2, size.height)
                )
                drawCircle(
                    colors.moon,
                    radius = sunRadius,
                    center = moonCenter + Offset(size.width / 2, size.height)
                )

                // terrain
                val terrain = Path().apply {
                    moveTo(0f, size.height * 2 / 3)
                    cubicTo(
                        size.width / 3, 0f,
                        size.width / 3, size.height,
                        size.width, size.height * 2 / 3
                    )
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                }
                drawPath(
                    terrain,
                    colors.grass
                        .copy(alpha = .5f)
                        .compositeOver(containerColor)
                )
            }
            .padding(12.dp, 6.dp),
        contentAlignment = Alignment.Center
    ) {
        ProvideTextStyle(Theme.textStyle.body.copy(fontWeight = FontWeight.Medium)) {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                content()
            }
        }
    }
}

@Preview
@Composable
private fun TimeButtonPreview(
    @PreviewParameter(TimeButtonParameter::class)
    parameter: Long
) = PreviewLayout {
    val format = remember { DateFormat.getTimeInstance(DateFormat.SHORT) }
    TimeButton(parameter) {
        Text(format.format(Date(parameter)))
    }
}

@Preview
@Composable
private fun TimeButtonAnimationPreview() = PreviewLayout {
    val transition = rememberInfiniteTransition()
    val time by transition.animateFloat(
        initialValue = 0f,
        targetValue = 86400000f,
        animationSpec = infiniteRepeatable(tween(5000))
    )
    val format = remember { DateFormat.getTimeInstance(DateFormat.SHORT) }
    TimeButton(time.toLong()) {
        Text(format.format(Date(time.toLong())))
    }
}

private class TimeButtonParameter : PreviewParameterProvider<Long> {
    override val values = sequence {
        yield(1672560000000)
        yield(1672570800000)
        yield(1672588800000)
        yield(1672599600000)
    }
}

object TimeButtonDefaults {
    @Composable
    fun colors() = TimeButtonColors()
}

data class TimeButtonColors(
    val day: Color = Color.White,
    val night: Color = Color.Black,
    val grass: Color = Color(0xFF2ADA16),
    val sun: Color = Color(0xFFF0D807),
    val moon: Color = Color(0xFFDFDFDF),
    val sky: Color = Color(0xff99ccff)
)