package movie.metropolis.app.util

import androidx.compose.foundation.*
import androidx.compose.foundation.pager.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.util.*
import movie.metropolis.app.screen2.booking.component.toOffset
import kotlin.math.absoluteValue
import kotlin.math.sign

@ExperimentalFoundationApi
fun Modifier.interpolatePage(
    state: PagerState,
    page: Int,
    scaleRange: ClosedFloatingPointRange<Float> = .8f..1f,
    alphaRange: ClosedFloatingPointRange<Float> = .5f..1f,
    rotation: Float = 15f,
    offset: DpOffset = DpOffset(16.dp, 32.dp),
    blur: Dp = 32.dp
) = composed {
    val pageOffset = with(state) { (currentPage - page) + currentPageOffsetFraction }
    val fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
    val sign = pageOffset.sign
    val density = LocalDensity.current
    val offsetPx = offset.toOffset(density)
    blur(lerp(blur, 0.dp, fraction), BlurredEdgeTreatment.Unbounded).graphicsLayer {
        rotationZ = lerp(-sign * rotation, 0f, fraction)
        alpha = lerp(alphaRange.start, alphaRange.endInclusive, fraction)
        scaleX = lerp(scaleRange.start, scaleRange.endInclusive, fraction)
        scaleY = scaleX
        translationY = lerp(offsetPx.y, 0f, fraction)
        translationX = lerp(offsetPx.x, 0f, fraction)
    }
}