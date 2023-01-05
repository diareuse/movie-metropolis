package movie.style

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
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