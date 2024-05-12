package movie.cinema.city

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import movie.cinema.city.adapter.MovieFromDatabase
import movie.cinema.city.adapter.TicketFromDatabase
import movie.cinema.city.persistence.CinemaDao
import movie.cinema.city.persistence.CinemaFromDatabase
import movie.cinema.city.persistence.CinemaStored
import movie.cinema.city.persistence.MovieDao
import movie.cinema.city.persistence.MovieStored
import movie.cinema.city.persistence.TicketDao
import movie.cinema.city.persistence.TicketStored

internal class CinemaCityStorage(
    client: CinemaCityClient,
    private val movie: MovieDao,
    private val ticket: TicketDao,
    private val cinema: CinemaDao
) : CinemaCityComposition(client) {

    override val events: CinemaCity.Events
        get() = Events(super.events)
    override val customers: CinemaCity.Customers
        get() = Customers(super.customers)
    override val cinemas: CinemaCity.Cinemas
        get() = Cinemas(super.cinemas)

    private inner class Events(
        private val origin: CinemaCity.Events
    ) : CinemaCity.Events by origin {
        override suspend fun getEvent(id: String): Movie = try {
            coroutineScope {
                val storedMovie = async { movie.selectMovie(id) }
                val storedCast = async { movie.selectCast(id) }
                val storedDirectors = async { movie.selectDirector(id) }
                val storedGenre = async { movie.selectGenre(id) }
                val storedImage = async { movie.selectImage(id) }
                val storedVideo = async { movie.selectVideo(id) }
                MovieFromDatabase(
                    storedMovie = storedMovie.await(),
                    storedCast = storedCast.await(),
                    storedDirectors = storedDirectors.await(),
                    storedGenre = storedGenre.await(),
                    storedImage = storedImage.await(),
                    storedVideo = storedVideo.await()
                )
            }
        } catch (ignore: Throwable) {
            origin.getEvent(id).also {
                movie.insert(MovieStored(it))
                val cast = it.cast
                    .map { i -> MovieStored.Cast(it.id, i) }
                val director = it.directors
                    .map { i -> MovieStored.Director(it.id, i) }
                val genre = it.genres
                    .map { i -> MovieStored.Genre(it.id, i) }
                val images = it.images
                    .map { i -> MovieStored.Image(it.id, i.width, i.height, i.url) }
                val video = it.videos
                    .map { i -> MovieStored.Video(it.id, i) }
                supervisorScope {
                    launch { movie.insertCast(cast) }
                    launch { movie.insertDirector(director) }
                    launch { movie.insertGenre(genre) }
                    launch { movie.insertImage(images) }
                    launch { movie.insertVideo(video) }
                }
            }
        }
    }

    private inner class Customers(
        private val origin: CinemaCity.Customers
    ) : CinemaCity.Customers by origin {
        // fixme this is gonna cache them forever though and they're never gonna update
        override suspend fun getTickets(): List<Ticket> = try {
            val tickets = ticket.selectTickets().ifEmpty { error("empty") }
            val cinemas = cinemas.getCinemas()
            tickets.parallelMap {
                TicketFromDatabase(
                    ticket = it,
                    ticketReservations = ticket.selectReservations(it.id),
                    cinema = cinemas.first { c -> c.id == it.cinema },
                    movie = events.getEvent(it.id)
                )
            }
        } catch (ignore: Throwable) {
            origin.getTickets().also { tickets ->
                ticket.insertTickets(tickets.map(::TicketStored))
                ticket.insertReservations(tickets.flatMap { ticket ->
                    ticket.venue.reservations.map {
                        TicketStored.Reservation(ticket.id, it.row, it.seat)
                    }
                })
            }
        }
    }

    private inner class Cinemas(
        private val origin: CinemaCity.Cinemas
    ) : CinemaCity.Cinemas by origin {
        override suspend fun getCinemas(): List<Cinema> = try {
            cinema.select().ifEmpty { error("empty") }.map(::CinemaFromDatabase)
        } catch (ignore: Throwable) {
            origin.getCinemas().also { cinemas ->
                cinema.insert(cinemas.map(::CinemaStored))
            }
        }
    }

}