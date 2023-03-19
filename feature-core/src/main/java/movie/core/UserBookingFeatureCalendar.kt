package movie.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import movie.calendar.CalendarWriter
import movie.calendar.EventMetadata
import movie.core.db.dao.MovieDao
import movie.core.model.Booking
import movie.core.preference.EventPreference
import kotlin.time.Duration.Companion.milliseconds

class UserBookingFeatureCalendar(
    private val origin: UserBookingFeature,
    private val writer: CalendarWriter.Factory,
    private val preference: EventPreference,
    private val scope: CoroutineScope,
    private val dao: MovieDao
) : UserBookingFeature by origin {

    override suspend fun get() = origin.get().onSuccess { bookings ->
        scope.launch {
            writeInCalendar(bookings)
        }
    }

    private suspend fun writeInCalendar(bookings: Sequence<Booking>) {
        val calendar = preference.calendarId ?: return
        val writer = writer.create(calendar)
        for (booking in bookings) {
            val metadata = EventMetadata(
                name = booking.name,
                start = booking.startsAt,
                duration = dao.getDuration(booking.movieId).milliseconds,
                description = null,
                location = booking.cinema.address.joinToString()
            )
            writer.write(metadata)
        }
    }

}