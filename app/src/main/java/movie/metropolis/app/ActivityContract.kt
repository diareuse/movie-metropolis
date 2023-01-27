package movie.metropolis.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import java.io.File

@Immutable
data class ActivityActions(
    val requestPermissions: suspend (permissions: Array<String>) -> Boolean,
    val actionView: (String) -> Unit,
    val actionShare: (File) -> Unit
) {

    companion object {

        val Default = ActivityActions(
            requestPermissions = { false },
            actionView = {},
            actionShare = {}
        )

    }

}

val LocalActivityActions = staticCompositionLocalOf {
    ActivityActions.Default
}

@Composable
fun ProvideActivityActions(
    actions: ActivityActions,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalActivityActions provides actions) {
        content()
    }
}