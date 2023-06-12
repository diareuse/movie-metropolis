package movie.metropolis.app

import androidx.compose.runtime.*
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