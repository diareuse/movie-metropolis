package movie.cinema.city

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import movie.cinema.city.adapter.CinemaFromResponse
import movie.cinema.city.adapter.CustomerFromResponse
import movie.cinema.city.adapter.MovieFromResponse
import movie.cinema.city.adapter.OccurrenceFromResponse
import movie.cinema.city.adapter.PromoCardFromResponse
import movie.cinema.city.adapter.TicketFromResponse
import movie.cinema.city.model.ConsentRemote
import movie.cinema.city.model.CustomerDataRequest
import movie.cinema.city.model.PasswordRequest
import movie.cinema.city.model.RegistrationRequest
import movie.cinema.city.model.ShowingType
import java.util.Date

internal open class CinemaCityComposition(
    private val client: CinemaCityClient
) : CinemaCity {

    override val customers: CinemaCity.Customers by lazy { Customers() }
    override val cinemas: CinemaCity.Cinemas by lazy { Cinemas() }
    override val events: CinemaCity.Events by lazy { Events() }

    override suspend fun create(details: UserDetails): CinemaCity.Customers {
        val request = RegistrationRequest(
            email = details.email,
            firstName = details.firstName,
            lastName = details.lastName,
            password = details.password,
            phone = details.phone,
            consent = ConsentRemote(details.marketing, details.premium)
        )
        client.register(request)
        return customers
    }

    private inner class Customers : CinemaCity.Customers {
        override suspend fun updatePassword(current: String, next: String) {
            client.updatePassword(PasswordRequest(current, next))
        }

        override suspend fun updateCustomer(modification: CustomerModification) {
            val update = CustomerDataRequest(
                consent = ConsentRemote(
                    modification.consent.marketing,
                    modification.consent.premium
                ),
                email = modification.email,
                firstName = modification.name.first,
                lastName = modification.name.last,
                favoriteCinema = modification.cinema?.id,
                phone = modification.phone,
                locale = modification.locale
            )
            client.updateUser(update)
        }

        override suspend fun getCustomer(): Customer = coroutineScope {
            val user = async { client.getUser() }
            val points = async { client.getPoints() }
            val cinema = async {
                val cinema = user.await().favoriteCinema
                if (cinema == null) null else cinemas.getCinemas().first { it.id == cinema }
            }
            val url = async { gravatarURL(user.await().email) }
            CustomerFromResponse(
                customer = user.await(),
                points = points.await(),
                cinema = cinema.await(),
                image = url.await()
            )
        }

        override suspend fun getTickets(): List<Ticket> = coroutineScope {
            val cinemas = async { cinemas.getCinemas() }
            client.getBookings().parallelMap { booking ->
                val detail = async { client.getBooking(booking.id) }
                val movie = async { events.getEvent(booking.movieId) }
                val cinema = async { cinemas.await().first { it.id == booking.cinemaId } }
                TicketFromResponse(
                    booking = booking,
                    detail = detail.await(),
                    movie = movie.await(),
                    cinema = cinema.await()
                )
            }
        }

        override suspend fun getToken(): String {
            return client.getToken().accessToken
        }
    }

    private inner class Cinemas : CinemaCity.Cinemas {
        override suspend fun getCinemas(): List<Cinema> {
            return client.getCinemas().map(::CinemaFromResponse)
        }

        override suspend fun getPromoCards(): List<PromoCard> {
            return client.getPromoCards().map(::PromoCardFromResponse)
        }
    }

    private inner class Events : CinemaCity.Events {
        override suspend fun getEvents(cinema: Cinema, date: Date): Map<Movie, List<Occurrence>> {
            val response = client.getEventsInCinema(cinema.id, date)
            val occurrences = response.events
            val movies = response.movies
            return movies.parallelMap { m ->
                events.getEvent(m.id) to occurrences.asSequence()
                    .filter { it.movieId == m.id }
                    .filterNot { it.soldOut }
                    .mapTo(mutableListOf()) { OccurrenceFromResponse(it, cinema) }
            }.toMap()
        }

        override suspend fun getEvents(future: Boolean): List<Movie> {
            val type = when (future) {
                true -> ShowingType.Upcoming
                else -> ShowingType.Current
            }
            return client.getMoviesByType(type).parallelMap {
                events.getEvent(it.id.key)
            }
        }

        override suspend fun getEvent(id: String): Movie {
            return client.getDetail(id).let(::MovieFromResponse)
        }
    }

}