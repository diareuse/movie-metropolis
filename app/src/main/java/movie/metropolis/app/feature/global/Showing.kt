package movie.metropolis.app.feature.global

import java.util.Date

data class Showing(
    val id: String,
    val cinema: Cinema,
    val startsAt: Date,
    val bookingUrl: String,
    val isEnabled: Boolean, // ie. is not sold out
    val auditorium: String
)