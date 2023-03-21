package movie.metropolis.app.model.adapter

import movie.core.adapter.MovieDetailFromId
import movie.core.model.Booking
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.MovieDetailView
import java.text.DateFormat
import java.util.Date

data class BookingViewExpiredFromFeature(
    private val booking: Booking.Expired
) : BookingView.Expired {

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
        get() = MovieDetailViewFromFeature(MovieDetailFromId(booking.id))
    override val isPaid: Boolean
        get() = booking.paidAt.before(Date())
    override val cinema: CinemaView
        get() = CinemaViewFromFeature(booking.cinema)

    override fun toString(): String {
        return "BookingView.Expired(id='$id', name='$name', date='$date', time='$time', movie=$movie, isPaid=$isPaid, cinema=$cinema)"
    }

}