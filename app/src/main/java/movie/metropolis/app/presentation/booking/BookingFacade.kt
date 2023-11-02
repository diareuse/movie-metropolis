package movie.metropolis.app.presentation.booking

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.facade.Image
import movie.metropolis.app.presentation.asLoadable

interface BookingFacade {

    val bookings: Flow<Result<List<BookingView>>>

    suspend fun getShareImage(view: BookingView): Image?

    fun refresh()

    companion object {

        fun BookingFacade.bookingsFlow(refresh: Flow<suspend () -> Unit>) = flow {
            emitAll(bookings.map { it.asLoadable() })
            refresh.collect {
                it()
                emitAll(bookings.map { it.asLoadable() })
            }
        }

    }

}