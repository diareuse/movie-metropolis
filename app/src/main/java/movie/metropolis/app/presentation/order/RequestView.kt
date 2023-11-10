package movie.metropolis.app.presentation.order

import androidx.compose.runtime.*
import kotlinx.collections.immutable.ImmutableMap

@Stable
data class RequestView(
    val url: String,
    val headers: ImmutableMap<String, String>
)