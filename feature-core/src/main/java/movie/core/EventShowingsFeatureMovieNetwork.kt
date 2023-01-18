package movie.core

import movie.core.adapter.ShowingFromResponse
import movie.core.model.Location
import movie.core.model.Movie
import movie.core.nwk.EventService
import java.util.Date

class EventShowingsFeatureMovieNetwork(
    private val movie: Movie,
    private val location: Location,
    private val service: EventService,
    private val cinema: EventCinemaFeature
) : EventShowingsFeature.Movie {

    override suspend fun get(date: Date, result: ResultCallback<CinemaWithShowings>) {
        cinema.get(location) { cinemas ->
            val output = cinemas.getOrThrow().associateWith { cinema ->
                service.getEventsInCinema(cinema.id, date)
                    .getOrThrow().body.events
                    .filter { it.movieId == movie.id }
                    .map { ShowingFromResponse(it, cinema) }
            }
            result(Result.success(output))
        }
    }

}

