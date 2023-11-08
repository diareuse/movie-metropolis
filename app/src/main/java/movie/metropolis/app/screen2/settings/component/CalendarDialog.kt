package movie.metropolis.app.screen2.settings.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import kotlinx.collections.immutable.toImmutableMap
import movie.style.layout.PreviewLayout
import movie.style.modifier.glow
import movie.style.modifier.surface
import movie.style.theme.Theme

@Deprecated("Use DialogBox.Container")
@Composable
fun CalendarDialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) = Dialog(
    onDismissRequest = onDismissRequest,
    properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
) {
    Box(
        modifier = Modifier
            .padding(24.dp)
            .surface(1.dp, Theme.container.card)
            .glow(Theme.container.card)
    ) {
        content()
    }
}

@Preview
@Composable
private fun CalendarDialogPreview() = PreviewLayout {
    CalendarDialog({}) {
        val items = CalendarViewParameter(2).values.toList().groupBy { it.account }.toImmutableMap()
        CalendarColumn(calendars = items, {})
    }
}