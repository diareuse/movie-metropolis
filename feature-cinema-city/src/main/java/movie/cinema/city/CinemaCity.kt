package movie.cinema.city

import java.util.Date

interface CinemaCity {

    val customers: Customers
    val cinemas: Cinemas
    val events: Events

    suspend fun create(details: UserDetails): Customers

    interface Customers {
        suspend fun updatePassword(current: String, next: String)
        suspend fun updateCustomer(modification: CustomerModification)
        suspend fun getCustomer(): Customer
        suspend fun getTickets(): List<Ticket>
        suspend fun getToken(): String
    }

    interface Cinemas {
        suspend fun getCinemas(): List<Cinema>
        suspend fun getPromoCards(): List<PromoCard>
    }

    interface Events {
        suspend fun getEvents(cinema: Cinema, date: Date): Map<Movie, List<Occurrence>>
        suspend fun getEvents(future: Boolean): List<Movie>
        suspend fun getEvent(id: String): Movie
    }

}