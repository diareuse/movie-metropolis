package movie.metropolis.app.screen.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import movie.metropolis.app.feature.global.Booking
import movie.metropolis.app.feature.global.UserFeature
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.asLoadable
import movie.metropolis.app.screen.booking.BookingFacade.Companion.bookingsFlow
import movie.metropolis.app.screen.cinema.CinemaViewFromFeature
import movie.metropolis.app.screen.detail.MovieDetailViewFromFeature
import movie.metropolis.app.screen.mapLoadable
import java.text.DateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    facade: BookingFacade
) : ViewModel() {

    private val items = facade.bookingsFlow
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 1)
    val expired = items
        .mapLoadable { it.filterIsInstance<BookingView.Expired>() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())
    val active = items
        .mapLoadable { it.filterIsInstance<BookingView.Active>() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())

}

interface BookingFacade {

    suspend fun getBookings(): Result<List<BookingView>>

    companion object {

        val BookingFacade.bookingsFlow
            get() = flow {
                emit(Loadable.loading())
                emit(getBookings().asLoadable())
            }

    }

}

class BookingFacadeFromFeature(
    private val feature: UserFeature
) : BookingFacade {

    override suspend fun getBookings() = feature.getBookings()
        .map { it.map(::BookingViewFromFeature) }

    @Suppress("FunctionName")
    private fun BookingViewFromFeature(booking: Booking) = when (booking) {
        is Booking.Active -> BookingViewActiveFromFeature(booking)
        is Booking.Expired -> BookingViewExpiredFromFeature(booking)
    }

}

class BookingFacadeRecover(
    private val origin: BookingFacade
) : BookingFacade {

    override suspend fun getBookings() =
        kotlin.runCatching { origin.getBookings().getOrThrow() }

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

        override fun toString(): String {
            return "Seat(row='$row', seat='$seat')"
        }

    }

    override fun toString(): String {
        return "BookingView.Active(id='$id', name='$name', date='$date', time='$time', movie=$movie, isPaid=$isPaid, cinema=$cinema, hall='$hall', seats=$seats)"
    }

}