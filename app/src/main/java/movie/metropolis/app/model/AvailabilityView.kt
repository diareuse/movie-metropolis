package movie.metropolis.app.model

interface AvailabilityView {

    val id: String
    val url: String
    val startsAt: String
    val isEnabled: Boolean

    interface Type {
        val types: List<String>
        val language: String
        val type get() = types.joinToString(" | ")
    }

}

