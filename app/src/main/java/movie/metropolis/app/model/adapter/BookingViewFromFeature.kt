package movie.metropolis.app.model.adapter

import movie.cinema.city.Ticket
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.presentation.booking.plus
import java.text.DateFormat
import java.util.Date
import kotlin.time.Duration.Companion.hours

data class BookingViewFromFeature(
    internal val booking: Ticket
) : BookingView {

    private val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)
    private val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT)

    override val id: String
        get() = booking.id
    override val name: String
        get() = booking.name
    override val date: String
        get() = dateFormat.format(booking.startsAt)
    override val time: String
        get() = timeFormat.format(booking.startsAt)
    override val movie: MovieDetailView
        get() = MovieDetailViewFromFeature(booking.movie)
    override val isPaid: Boolean
        get() = booking.paidAt?.before(Date()) == true
    override val cinema: CinemaView
        get() = CinemaViewFromFeature(booking.cinema)
    override val expired: Boolean
        get() = Date().after(booking.startsAt + (booking.movie.length ?: 3.hours))
    override val hall: String
        get() = booking.venue.name
    override val seats: List<BookingView.Seat>
        get() = booking.venue.reservations.map(BookingViewFromFeature::SeatFromFeature)

    override fun origin(): BookingView {
        return this
    }

    data class SeatFromFeature(
        private val feature: Ticket.Reservation
    ) : BookingView.Seat {

        override val row: String
            get() = feature.row
        override val seat: String
            get() = feature.seat

        override fun toString(): String {
            return "Seat(row='$row', seat='$seat')"
        }

    }

    override fun toString(): String {
        return "BookingView.Active(id='$id', name='$name', date='$date', time='$time', movie=$movie, isPaid=$isPaid, cinema=$cinema, hall='$hall', seats=$seats)"
    }

}