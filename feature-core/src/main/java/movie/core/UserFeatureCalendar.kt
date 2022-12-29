package movie.core

import movie.calendar.CalendarWriter
import movie.calendar.EventMetadata
import movie.core.preference.EventPreference

class UserFeatureCalendar(
    private val origin: UserFeature,
    private val writer: CalendarWriter.Factory,
    private val preference: EventPreference
) : UserFeature by origin {

    override suspend fun getBookings() = origin.getBookings().onSuccess {
        val calendar = preference.calendarId ?: return@onSuccess
        val writer = writer.create(calendar)
        for (booking in it) {
            val metadata = EventMetadata(
                name = booking.name,
                start = booking.startsAt,
                duration = booking.movie.duration,
                description = null,
                location = booking.cinema.address.joinToString()
            )
            writer.write(metadata)
        }
    }

}