package movie.metropolis.app.screen.booking

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.asLoadable

interface BookingFacade {

    suspend fun getBookings(): Result<List<BookingView>>
    suspend fun refresh()

    companion object {

        fun BookingFacade.bookingsFlow(refresh: Flow<suspend () -> Unit>) = flow {
            emit(getBookings().asLoadable())
            refresh.collect {
                emit(Loadable.loading())
                it()
                emit(getBookings().asLoadable())
            }
        }

    }

}