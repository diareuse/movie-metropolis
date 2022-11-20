package movie.metropolis.app.feature.user

import java.util.Date

data class Booking(
    val id: String,
    val name: String,
    val startsAt: Date,
    val paidAt: Date,
    val distributorCode: String,
    val eventId: String
)