package movie.metropolis.app.screen2.booking.component

import android.content.res.Configuration
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.semantics.*
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
    onClick: () -> Unit,
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
    Box(
        modifier = modifier
            .clip(Theme.container.button)
            .clickable(role = Role.Button, onClick = onClick)
            .drawWithCache {
                onDrawBehind {
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
                    drawRect(containerColor)
                    drawRect(colors.sky, alpha = .5f)
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
                    drawPath(path = terrain, brush = colors.grass)
                }
            }
            .background(Theme.color.container.background.copy(.5f))
            .padding(12.dp, 6.dp),
        contentAlignment = Alignment.Center
    ) {
        ProvideTextStyle(Theme.textStyle.body.copy(fontWeight = FontWeight.Medium)) {
            content()
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun TimeButtonAnimationPreview() = PreviewLayout {
    val transition = rememberInfiniteTransition()
    val time by transition.animateFloat(
        initialValue = 0f,
        targetValue = 86400000f,
        animationSpec = infiniteRepeatable(tween(5000))
    )
    val format = remember { DateFormat.getTimeInstance(DateFormat.SHORT) }
    TimeButton(modifier = Modifier.width(90.dp), time = time.toLong(), onClick = {}) {
        Text(format.format(Date(time.toLong())))
    }
}

object TimeButtonDefaults {
    @Composable
    fun colors() = TimeButtonColors()
}

data class TimeButtonColors(
    val day: Color = Color.White,
    val night: Color = Color.Black,
    val grass: Brush = Brush.verticalGradient(listOf(Color(0xFF084401), Color(0xFF2ADA16))),
    val sun: Brush = Brush.verticalGradient(listOf(Color(0xFFF0D807), Color(0xFFE9A700))),
    val moon: Brush = Brush.verticalGradient(listOf(Color(0xFFDFDFDF), Color(0xFF838383))),
    val sky: Brush = Brush.verticalGradient(listOf(Color(0xFF005FBD), Color(0xFF99CCFF)))
)