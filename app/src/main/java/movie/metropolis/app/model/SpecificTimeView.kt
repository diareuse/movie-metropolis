package movie.metropolis.app.model

interface SpecificTimeView {
    val time: Long
    val formatted: String
    val url: String
    val isEnabled: Boolean
}