package movie.metropolis.app.model.adapter

import movie.core.model.Booking
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.MovieDetailView
import java.text.DateFormat
import java.util.Date

data class BookingViewActiveFromFeature(
    internal val booking: Booking.Active
) : BookingView.Active {

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
        get() = TODO("Add loading movie detail")//MovieDetailViewFromFeature(booking.movie)
    override val isPaid: Boolean
        get() = booking.paidAt.before(Date())
    override val cinema: CinemaView
        get() = CinemaViewFromFeature(booking.cinema)
    override val hall: String
        get() = booking.hall
    override val seats: List<BookingView.Active.Seat>
        get() = booking.seats.map(BookingViewActiveFromFeature::SeatFromFeature)

    data class SeatFromFeature(
        private val feature: Booking.Active.Seat
    ) : BookingView.Active.Seat {

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