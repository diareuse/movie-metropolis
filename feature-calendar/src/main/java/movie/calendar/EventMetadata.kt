package movie.calendar

import java.util.Date
import kotlin.time.Duration

data class EventMetadata(
    val name: String,
    val start: Date,
    val duration: Duration,
    val description: String?,
    val location: String?
) {

    val end get() = Date(start.time + duration.inWholeMilliseconds)

}