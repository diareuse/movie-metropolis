package movie.core

import movie.calendar.CalendarWriter
import movie.calendar.EventMetadata
import movie.core.db.dao.MovieDao
import movie.core.model.Booking
import movie.core.preference.EventPreference

class UserBookingFeatureCalendar(
    private val origin: UserBookingFeature,
    private val writer: CalendarWriter.Factory,
    private val preference: EventPreference,
    private val dao: MovieDao
) : UserBookingFeature by origin {

    override suspend fun get(): Sequence<Booking> = origin.get().also { bookings ->
        writeInCalendar(bookings)
    }

    private suspend fun writeInCalendar(bookings: Sequence<Booking>) {
        val calendar = preference.calendarId ?: return
        val writer = writer.create(calendar)
        for (booking in bookings) {
            val duration = dao.select(booking.movieId)?.duration ?: continue
            val metadata = EventMetadata(
                name = booking.name,
                start = booking.startsAt,
                duration = duration,
                description = null,
                location = booking.cinema.address.joinToString()
            )
            writer.write(metadata)
        }
    }

}