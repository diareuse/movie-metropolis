package movie.metropolis.app.model

import androidx.compose.runtime.Stable

@Stable
data class Filter(
    val isSelected: Boolean,
    val value: String
) {

    enum class Type {
        Language, Projection
    }

}