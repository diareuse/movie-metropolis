package movie.metropolis.app.model

import movie.metropolis.app.feature.global.Booking
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
        get() = MovieDetailViewFromFeature(booking.movie)
    override val isPaid: Boolean
        get() = booking.paidAt.before(Date())
    override val cinema: CinemaView
        get() = CinemaViewFromFeature(booking.cinema)

    override fun toString(): String {
        return "BookingView.Expired(id='$id', name='$name', date='$date', time='$time', movie=$movie, isPaid=$isPaid, cinema=$cinema)"
    }

}