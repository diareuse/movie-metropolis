package movie.cinema.city

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

internal class CinemaCityThreads(
    private val origin: CinemaCity
) : CinemaCity {
    override val customers: CinemaCity.Customers
        get() = Customers(origin.customers)
    override val cinemas: CinemaCity.Cinemas
        get() = Cinemas(origin.cinemas)
    override val events: CinemaCity.Events
        get() = Events(origin.events)

    override suspend fun create(details: UserDetails) = io { origin.create(details) }

    // ---

    private class Customers(
        private val origin: CinemaCity.Customers
    ) : CinemaCity.Customers {
        override suspend fun updatePassword(current: String, next: String) = io {
            origin.updatePassword(current, next)
        }

        override suspend fun updateCustomer(modification: CustomerModification) = io {
            origin.updateCustomer(modification)
        }

        override suspend fun getCustomer() = io {
            origin.getCustomer()
        }

        override suspend fun getTickets() = io {
            origin.getTickets()
        }

        override suspend fun getToken() = io {
            origin.getToken()
        }
    }

    private class Cinemas(
        private val origin: CinemaCity.Cinemas
    ) : CinemaCity.Cinemas {
        override suspend fun getCinemas() = io {
            origin.getCinemas()
        }

        override suspend fun getPromoCards() = io {
            origin.getPromoCards()
        }
    }

    private class Events(
        private val origin: CinemaCity.Events
    ) : CinemaCity.Events {
        override suspend fun getEvents(cinema: Cinema, date: Date) = io {
            origin.getEvents(cinema, date)
        }

        override suspend fun getEvents(future: Boolean) = io {
            origin.getEvents(future)
        }

        override suspend fun getEvent(id: String) = io {
            origin.getEvent(id)
        }
    }

    // ---

    companion object {
        private suspend inline fun <T> io(
            noinline block: suspend CoroutineScope.() -> T
        ): T = withContext(Dispatchers.IO, block = block)
    }

}