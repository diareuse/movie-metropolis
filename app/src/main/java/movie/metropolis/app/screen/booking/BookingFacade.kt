package movie.metropolis.app.screen.booking

import kotlinx.coroutines.flow.flow
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.asLoadable

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