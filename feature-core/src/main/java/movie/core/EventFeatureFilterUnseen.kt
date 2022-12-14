package movie.core

import movie.core.db.dao.BookingDao
import movie.core.model.Cinema
import movie.core.model.MoviePreview
import movie.core.preference.EventPreference
import java.util.Date

class EventFeatureFilterUnseen(
    origin: EventFeature,
    private val preference: EventPreference,
    booking: BookingDao
) : EventFeature by origin {

    private val positive = Wrapper(origin, booking)
    private val negative = origin

    override suspend fun getShowings(cinema: Cinema, at: Date) = when (preference.filterSeen) {
        true -> positive.getShowings(cinema, at)
        else -> negative.getShowings(cinema, at)
    }

    override suspend fun getCurrent() = when (preference.filterSeen) {
        true -> positive.getCurrent()
        else -> negative.getCurrent()
    }

    override suspend fun getUpcoming() = when (preference.filterSeen) {
        true -> positive.getUpcoming()
        else -> negative.getUpcoming()
    }

    private class Wrapper(
        private val origin: EventFeature,
        private val bookingDao: BookingDao
    ) : EventFeature by origin {

        override suspend fun getShowings(cinema: Cinema, at: Date): Result<MovieWithShowings> {
            val bookings = getSeenMovies()
            return origin.getShowings(cinema, at).map { showings ->
                showings.filterNot { it.key.id in bookings }
            }
        }

        override suspend fun getCurrent(): Result<Iterable<MoviePreview>> {
            val bookings = getSeenMovies()
            return origin.getCurrent().map { previews ->
                previews.filterNot { it.id in bookings }
            }
        }

        override suspend fun getUpcoming(): Result<Iterable<MoviePreview>> {
            val bookings = getSeenMovies()
            return origin.getUpcoming().map { previews ->
                previews.filterNot { it.id in bookings }
            }
        }

        // ---

        private suspend fun getSeenMovies(): List<String> {
            return bookingDao.selectAll().map { it.movieId }
        }

    }

}