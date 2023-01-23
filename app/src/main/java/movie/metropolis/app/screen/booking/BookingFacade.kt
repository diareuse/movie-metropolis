package movie.metropolis.app.screen.booking

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.debounce
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.facade.Image
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.asLoadable
import kotlin.time.Duration.Companion.seconds

interface BookingFacade {

    suspend fun getBookings(): Result<List<BookingView>>
    suspend fun refresh()
    suspend fun getImage(view: BookingView): Image?

    companion object {

        fun BookingFacade.bookingsFlow(refresh: Flow<suspend () -> Unit>) = channelFlow {
            send(getBookings().asLoadable())
            refresh.collect {
                send(Loadable.loading())
                it()
                send(getBookings().asLoadable())
            }
        }.debounce(1.seconds)

    }

}