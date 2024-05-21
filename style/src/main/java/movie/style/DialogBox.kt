package movie.style

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import movie.style.layout.PreviewLayout
import movie.style.layout.alignForLargeScreen
import movie.style.modifier.surface
import movie.style.theme.Theme

@Composable
fun DialogBox(
    visible: Boolean,
    dialog: @Composable DialogScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val blur by animateDpAsState(targetValue = if (visible) 32.dp else 0.dp)
    Box(modifier = modifier.blur(blur), propagateMinConstraints = true) {
        content()
    }
    if (visible) dialog(DialogScopeImpl)
}

interface DialogScope

internal object DialogScopeImpl : DialogScope

@Suppress("UnusedReceiverParameter")
@Composable
fun DialogScope.Container(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) = Dialog(
    onDismissRequest = onDismissRequest,
    properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
) {
    BackHandler {
        onDismissRequest()
    }
    Box(
        modifier = modifier
            .padding(24.dp)
            .alignForLargeScreen()
            .surface(1.dp, Theme.container.card),
        propagateMinConstraints = true
    ) {
        content()
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun DialogBoxPreview() = PreviewLayout {
    DialogBox(
        visible = false,
        dialog = { Text("Dialog") }
    ) {
        Box(Modifier)
    }
}