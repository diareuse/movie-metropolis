package movie.core

import movie.core.db.dao.ShowingDao
import movie.core.model.Location
import movie.core.model.Movie
import movie.core.util.dayEnd
import movie.core.util.dayStart
import java.util.Date

class EventShowingsFeatureMovieDatabase(
    private val movie: Movie,
    private val location: Location,
    private val showing: ShowingDao,
    private val cinema: EventCinemaFeature
) : EventShowingsFeature.Movie {

    override suspend fun get(date: Date, result: ResultCallback<CinemaWithShowings>) {
        cinema.get(location) { cinemas ->
            val output = cinemas.getOrThrow().associateWith { cinema ->
                showing.selectByCinema(
                    rangeStart = date.dayStart.coerceAtLeast(Date()).time,
                    rangeEnd = date.dayEnd.time,
                    cinema = cinema.id,
                    movie = movie.id
                ).map { ShowingFromDatabase(it, cinema) }
            }
            result(Result.success(output))
        }
    }

}