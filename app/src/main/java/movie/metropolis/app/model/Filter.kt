package movie.metropolis.app.model

import androidx.compose.runtime.*

@Immutable
data class Filter(
    val isSelected: Boolean,
    val value: String
) {

    enum class Type {
        Language, Projection
    }

}