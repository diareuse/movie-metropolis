package movie.metropolis.app.screen.booking

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy

@Composable
fun AppDialog(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(visible = isVisible) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(securePolicy = SecureFlagPolicy.SecureOn),
            content = content
        )
    }
}