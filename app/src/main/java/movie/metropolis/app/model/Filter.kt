package movie.metropolis.app.model

data class Filter(
    val isSelected: Boolean,
    val value: String
) {

    enum class Type {
        Language, Projection
    }

}