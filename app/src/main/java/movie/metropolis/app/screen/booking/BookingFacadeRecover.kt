package movie.metropolis.app.screen.booking

import movie.metropolis.app.model.BookingView
import java.io.File

class BookingFacadeRecover(
    private val origin: BookingFacade
) : BookingFacade {

    override suspend fun getBookings() =
        kotlin.runCatching { origin.getBookings().getOrThrow() }

    override suspend fun refresh() {
        kotlin.runCatching { origin.refresh() }
    }

    override suspend fun saveAsFile(view: BookingView): File {
        return origin.runCatching { saveAsFile(view) }.getOrDefault(File("/"))
    }

}