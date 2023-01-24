package movie.core

import movie.calendar.CalendarWriter
import movie.calendar.EventMetadata
import movie.core.model.Booking
import movie.core.preference.EventPreference

class UserBookingFeatureCalendar(
    private val origin: UserBookingFeature,
    private val writer: CalendarWriter.Factory,
    private val preference: EventPreference
) : UserBookingFeature by origin {

    override suspend fun get(callback: ResultCallback<List<Booking>>) {
        var value: Result<List<Booking>> = Result.failure(IllegalStateException())
        origin.get {
            value = it
            callback(it)
        }
        val bookings = value.getOrNull() ?: return
        val calendar = preference.calendarId ?: return
        val writer = writer.create(calendar)
        for (booking in bookings) {
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