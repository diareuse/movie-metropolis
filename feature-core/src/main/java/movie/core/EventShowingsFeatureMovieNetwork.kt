package movie.core

import movie.core.adapter.ShowingFromResponse
import movie.core.model.Location
import movie.core.model.Movie
import movie.core.nwk.EventService
import movie.core.util.requireNotEmpty
import java.util.Date

class EventShowingsFeatureMovieNetwork(
    private val movie: Movie,
    private val location: Location,
    private val service: EventService,
    private val cinema: EventCinemaFeature
) : EventShowingsFeature.Movie {

    override suspend fun get(date: Date): Result<CinemaWithShowings> = cinema.get(location)
        .mapCatching { it.requireNotEmpty() }
        .recoverCatching { cinema.get(null).getOrThrow() }
        .mapCatching { cinemas ->
            cinemas.associateWith { cinema ->
                service.getEventsInCinema(cinema.id, date)
                    .map { it.body.events }
                    .getOrDefault(emptyList())
                    .filter { it.movieId == movie.id }
                    .map { ShowingFromResponse(it, cinema) }
            }
        }

}