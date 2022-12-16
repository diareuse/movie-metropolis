package movie.metropolis.app.model

interface AvailabilityView {

    val id: String
    val url: String
    val startsAt: String
    val isEnabled: Boolean

    interface Type {
        val type: String
        val language: String
    }

}

