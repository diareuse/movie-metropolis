package movie.core

import movie.core.adapter.BookingActiveFromTicket
import movie.core.adapter.MovieFromId
import movie.core.model.Booking

class UserFeatureDrainTickets(
    private val origin: UserFeature,
    private val event: EventFeature,
    private val store: TicketStore
) : UserFeature by origin {

    override suspend fun getBookings(): Result<Iterable<Booking>> {
        return origin.getBookings().map { it + getStored() }
    }

    private suspend fun getStored() = store.getAll().mapNotNull { ticket ->
        val movie = event.getDetail(MovieFromId(ticket.movieId)).getOrNull()
            ?: return@mapNotNull null
        val cinema = event.getCinemas(null).getOrNull()?.firstOrNull { it.id == ticket.cinemaId }
            ?: return@mapNotNull null
        BookingActiveFromTicket(ticket, movie, cinema)
    }

}