package movie.core

import movie.core.EventCinemaFeature.Companion.get
import movie.core.EventDetailFeature.Companion.get
import movie.core.adapter.BookingActiveFromTicket
import movie.core.adapter.MovieFromId
import movie.core.model.Booking

class UserBookingFeatureDrainTickets(
    private val origin: UserBookingFeature,
    private val movie: EventDetailFeature,
    private val cinema: EventCinemaFeature,
    private val store: TicketStore
) : UserBookingFeature by origin {

    override suspend fun get(callback: ResultCallback<List<Booking>>) {
        origin.get(callback.thenMap {
            it + getStored()
        })
    }

    private suspend fun getStored() = store.getAll().mapNotNull { ticket ->
        val movie = this.movie.get(MovieFromId(ticket.movieId)).getOrNull()
            ?: return@mapNotNull null
        val cinemas = this.cinema.get(null).getOrNull()
        val cinema = cinemas?.firstOrNull { it.id == ticket.cinemaId }
            ?: return@mapNotNull null

        BookingActiveFromTicket(
            ticket = ticket,
            movie = movie,
            cinema = cinema
        )
    }

}