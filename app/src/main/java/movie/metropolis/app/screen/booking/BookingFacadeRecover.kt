package movie.metropolis.app.screen.booking

class BookingFacadeRecover(
    private val origin: BookingFacade
) : BookingFacade {

    override suspend fun getBookings() =
        kotlin.runCatching { origin.getBookings().getOrThrow() }

    override suspend fun refresh() {
        kotlin.runCatching { origin.refresh() }
    }

}