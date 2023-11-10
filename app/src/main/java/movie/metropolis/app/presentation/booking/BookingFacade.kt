package movie.metropolis.app.presentation.booking

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.facade.Image

interface BookingFacade {

    val bookings: Flow<List<BookingView>>

    suspend fun getShareImage(view: BookingView): Image?

    fun refresh()

    companion object {

        fun BookingFacade.bookingsFlow(refresh: Flow<suspend () -> Unit>) = flow {
            emitAll(bookings)
            refresh.collect {
                it()
                emitAll(bookings)
            }
        }

    }

}