package movie.metropolis.app.model.adapter

import movie.cinema.city.Ticket
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.util.plus
import java.text.DateFormat
import java.util.Date
import kotlin.time.Duration.Companion.minutes

data class BookingViewFromTicket(
    private val ticket: Ticket
) : BookingView {

    private val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT)
    private val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT)

    override val id: String
        get() = ticket.id
    override val name: String
        get() = ticket.name
    override val date: String
        get() = dateFormat.format(ticket.startsAt)
    override val time: String
        get() = timeFormat.format(ticket.startsAt)
    override val isPaid: Boolean
        get() = ticket.paidAt != null
    override val movie: MovieDetailView
        get() = MovieDetailViewFromMovie(ticket.movie)
    override val cinema: CinemaView
        get() = CinemaViewFromCinema(ticket.cinema)
    override val expired: Boolean
        get() = Date().after(ticket.startsAt + (ticket.movie.length ?: 90.minutes))
    override val hall: String
        get() = ticket.venue.name
    override val seats: List<BookingView.Seat>
        get() = ticket.venue.reservations.map(BookingViewFromTicket::Seat)

    override fun origin(): BookingView = this

    private data class Seat(
        private val reservation: Ticket.Reservation
    ) : BookingView.Seat {
        override val row: String
            get() = reservation.row
        override val seat: String
            get() = reservation.seat
    }

}