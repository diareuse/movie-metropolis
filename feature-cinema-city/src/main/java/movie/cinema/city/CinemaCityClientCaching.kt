package movie.cinema.city

import movie.cinema.city.model.BookingDetailResponse
import movie.cinema.city.model.CinemaResponse
import movie.cinema.city.model.ExtendedMovieResponse
import movie.cinema.city.model.MovieDetailResponse
import movie.cinema.city.model.MovieEventResponse
import movie.cinema.city.model.ShowingType
import java.util.Date

internal class CinemaCityClientCaching(
    private val origin: CinemaCityClient
) : CinemaCityClient by origin {

    private var cinemas = null as List<CinemaResponse>?
    private val booking = lruCache<String, BookingDetailResponse>(maxSize = 10)
    private val events = lruCache<CinemaTarget, MovieEventResponse>(
        maxSize = 200,
        sizeOf = { _, v -> v.events.size + v.movies.size }
    )
    private val details = lruCache<String, MovieDetailResponse>(200)
    private val movies = lruCache<ShowingType, List<ExtendedMovieResponse>>(
        maxSize = 200,
        sizeOf = { _, v -> v.size }
    )

    override suspend fun getCinemas(): List<CinemaResponse> {
        return cinemas ?: origin.getCinemas().also { cinemas = it.takeUnless { it.isEmpty() } }
    }

    override suspend fun getBooking(id: String) =
        booking.get(id) ?: origin.getBooking(id).also { booking.put(id, it) }

    override suspend fun getEventsInCinema(cinema: String, date: Date): MovieEventResponse {
        val key = CinemaTarget(cinema, date)
        return events.get(key) ?: origin.getEventsInCinema(cinema, date)
            .also { events.put(key, it) }
    }

    override suspend fun getDetail(id: String): MovieDetailResponse {
        return details.get(id) ?: origin.getDetail(id).also { details.put(id, it) }
    }

    override suspend fun getMoviesByType(type: ShowingType): List<ExtendedMovieResponse> {
        return movies.get(type) ?: origin.getMoviesByType(type).also { movies.put(type, it) }
    }

    // ---

    private data class CinemaTarget(
        val cinema: String,
        val date: Date
    )

}