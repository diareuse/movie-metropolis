package movie.style

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.*
import movie.style.haptic.hapticClick

@Composable
fun EllipsisText(
    text: String,
    maxLines: Int,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    startState: Boolean = false
) {
    var expanded by rememberSaveable(text) {
        mutableStateOf(startState)
    }
    Text(
        modifier = modifier
            .animateContentSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = hapticClick { expanded = !expanded }
            ),
        text = text,
        style = style,
        overflow = TextOverflow.Ellipsis,
        maxLines = if (expanded) Int.MAX_VALUE else maxLines
    )
}