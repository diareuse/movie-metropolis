package movie.metropolis.app.screen.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import movie.metropolis.app.feature.global.Booking
import movie.metropolis.app.feature.global.UserFeature
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.asLoadable
import movie.metropolis.app.screen.cinema.CinemaViewFromFeature
import movie.metropolis.app.screen.detail.MovieDetailViewFromFeature
import movie.metropolis.app.screen.map
import java.text.DateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val user: UserFeature
) : ViewModel() {

    private val items = flow { emit(user.getBookings()) }
        .map { it.map { it.map(::BookingViewFromFeature) }.asLoadable() }
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed())
    val expired = items
        .map { it.map { it.filterIsInstance<BookingView.Expired>() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())
    val active = items
        .map { it.map { it.filterIsInstance<BookingView.Active>() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())

    private fun BookingViewFromFeature(booking: Booking) = when (booking) {
        is Booking.Active -> BookingViewActiveFromFeature(booking)
        is Booking.Expired -> BookingViewExpiredFromFeature(booking)
    }

}

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
    override val isExpired: Boolean
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = booking.startsAt
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            return Date().after(calendar.time)
        }
    override val movie: MovieDetailView
        get() = MovieDetailViewFromFeature(booking.movie)
    override val isPaid: Boolean
        get() = booking.paidAt.before(Date())
    override val cinema: CinemaView
        get() = CinemaViewFromFeature(booking.cinema)

}

data class BookingViewActiveFromFeature(
    private val booking: Booking.Active
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
    override val isExpired: Boolean
        get() {
            val calendar = Calendar.getInstance()
            calendar.time = booking.startsAt
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            return Date().after(calendar.time)
        }
    override val movie: MovieDetailView
        get() = MovieDetailViewFromFeature(booking.movie)
    override val isPaid: Boolean
        get() = booking.paidAt.before(Date())
    override val cinema: CinemaView
        get() = CinemaViewFromFeature(booking.cinema)
    override val hall: String
        get() = booking.hall
    override val seats: List<BookingView.Active.Seat>
        get() = booking.seats.map(::SeatFromFeature)

    data class SeatFromFeature(
        private val feature: Booking.Active.Seat
    ) : BookingView.Active.Seat {
        override val row: String
            get() = feature.row
        override val seat: String
            get() = feature.seat
    }

}