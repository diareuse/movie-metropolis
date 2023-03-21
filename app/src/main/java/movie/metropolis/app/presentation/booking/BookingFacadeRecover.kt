package movie.metropolis.app.presentation.booking

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import movie.core.Recoverable
import movie.log.logSevere
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.facade.Image

class BookingFacadeRecover(
    private val origin: BookingFacade
) : BookingFacade, Recoverable {

    override val bookings: Flow<Result<List<BookingView>>> = flow {
        kotlin.runCatching {
            origin.bookings.collect(this)
        }.onFailure {
            emit(Result.failure(it))
        }
    }

    override fun refresh() {
        origin.runCatching { refresh() }.logSevere()
    }

    override suspend fun getShareImage(view: BookingView): Image? {
        return origin.runCatching { getShareImage(view) }.logSevere().getOrNull()
    }

}