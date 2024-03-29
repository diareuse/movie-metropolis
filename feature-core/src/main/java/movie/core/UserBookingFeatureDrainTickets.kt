package movie.core

import movie.core.adapter.BookingFromTicket
import movie.core.adapter.MovieFromId
import movie.core.model.Booking

class UserBookingFeatureDrainTickets(
    private val origin: UserBookingFeature,
    private val movie: EventDetailFeature,
    private val cinema: EventCinemaFeature,
    private val store: TicketStore
) : UserBookingFeature by origin {

    override suspend fun get(): Sequence<Booking> = origin.get().let {
        val stored = getStored()
        sequence {
            yieldAll(it)
            yieldAll(stored)
        }
    }

    private suspend fun getStored() = store.getAll().mapNotNull { ticket ->
        val movie = this.movie.runCatching { get(MovieFromId(ticket.movieId)) }.getOrNull()
            ?: return@mapNotNull null
        val cinemas = this.cinema.runCatching { get(null) }.getOrNull()
        val cinema = cinemas?.firstOrNull { it.id == ticket.cinemaId }
            ?: return@mapNotNull null

        BookingFromTicket(
            ticket = ticket,
            movie = movie,
            cinema = cinema
        )
    }

}