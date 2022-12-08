package movie.core.adapter

import movie.core.db.model.BookingStored
import movie.core.db.model.CinemaStored
import movie.core.db.model.MovieDetailView
import movie.core.db.model.MovieMediaView
import movie.core.model.Booking
import movie.core.model.Cinema
import movie.core.model.MovieDetail
import java.util.Date

data class BookingExpiredFromDatabase(
    private val bookingStored: BookingStored,
    private val movieStored: MovieDetailView,
    private val cinemaStored: CinemaStored,
    private val mediaStored: List<MovieMediaView>
) : Booking.Expired {
    override val id: String
        get() = bookingStored.id
    override val name: String
        get() = bookingStored.name
    override val startsAt: Date
        get() = bookingStored.startsAt
    override val paidAt: Date
        get() = bookingStored.paidAt
    override val movie: MovieDetail
        get() = MovieDetailFromDatabase(movieStored, mediaStored)
    override val eventId: String
        get() = bookingStored.eventId
    override val cinema: Cinema
        get() = CinemaFromDatabase(cinemaStored)
}