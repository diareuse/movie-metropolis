package movie.metropolis.app.model

import androidx.compose.runtime.Stable

@Stable
interface AvailabilityView {

    val id: String
    val url: String
    val startsAt: String
    val isEnabled: Boolean

    @Stable
    interface Type {
        val types: List<String>
        val language: String
        val type get() = types.joinToString(" | ")
    }

}

