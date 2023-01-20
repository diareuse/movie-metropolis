package movie.core

import movie.core.adapter.BookingActiveFromTicket
import movie.core.adapter.MovieFromId
import movie.core.model.Booking
import movie.core.model.Cinema
import movie.core.model.MovieDetail

class UserFeatureDrainTickets(
    private val origin: UserFeature,
    private val movie: EventDetailFeature,
    private val cinema: EventCinemaFeature,
    private val store: TicketStore
) : UserFeature by origin {

    override suspend fun getBookings(): Result<Iterable<Booking>> {
        return origin.getBookings().map { it + getStored() }
    }

    private suspend fun getStored() = store.getAll().mapNotNull { ticket ->
        var movie: Result<MovieDetail> = Result.failure(IllegalArgumentException())
        this.movie.get(MovieFromId(ticket.movieId)) {
            movie = it
        }

        var cinema: Result<Cinema> = Result.failure(IllegalArgumentException())
        this.cinema.get(null) {
            cinema = it.mapCatching { it.first { it.id == ticket.cinemaId } }
        }

        BookingActiveFromTicket(
            ticket = ticket,
            movie = movie.getOrNull() ?: return@mapNotNull null,
            cinema = cinema.getOrNull() ?: return@mapNotNull null
        )
    }

}