package movie.metropolis.app.screen.booking.component

import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TicketActions(
    actions: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    state: SwipeableState<Int> = rememberSwipeableState(initialValue = 0),
    content: @Composable () -> Unit
) {
    var anchorSize by remember { mutableStateOf(1f) }
    val anchors = remember(anchorSize) { mapOf(0f to 0, anchorSize to 1) }
    Box(
        modifier = modifier.swipeable(
            state = state,
            anchors = anchors,
            thresholds = { _, _ -> FractionalThreshold(1f) },
            orientation = Orientation.Vertical
        ),
        contentAlignment = Alignment.TopCenter
    ) {
        Row(
            modifier = Modifier.onPlaced { anchorSize = it.size.width.toFloat() },
            verticalAlignment = Alignment.CenterVertically,
            content = actions
        )
        Box(
            modifier = Modifier.offset {
                val offset = state.offset.value.takeUnless { it.isNaN() } ?: 0f
                IntOffset(0, offset.roundToInt())
            }
        ) {
            content()
        }
    }

    // revert state back to initial
    LaunchedEffect(state.currentValue) {
        delay(5000)
        if (state.currentValue != 0)
            state.animateTo(0)
    }
}
