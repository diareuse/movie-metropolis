package movie.metropolis.app.model

import androidx.compose.runtime.*

@Immutable
interface SpecificTimeView {
    val time: Long
    val formatted: String
    val url: String
    val isEnabled: Boolean
}