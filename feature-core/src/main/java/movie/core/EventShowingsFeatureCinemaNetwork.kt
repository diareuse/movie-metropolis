package movie.core

import movie.core.adapter.MovieReferenceFromResponse
import movie.core.adapter.ShowingFromResponse
import movie.core.model.Cinema
import movie.core.model.MovieReference
import movie.core.model.Showing
import movie.core.nwk.EventService
import java.util.Date

class EventShowingsFeatureCinemaNetwork(
    private val service: EventService,
    private val cinema: Cinema
) : EventShowingsFeature.Cinema {

    override suspend fun get(date: Date, result: ResultCallback<MovieWithShowings>) {
        val response = service.getEventsInCinema(cinema.id, date).getOrThrow()
        val events = response.body.events
        val movies = response.body.movies.associateBy { it.id.lowercase() }
        val output = buildMap<MovieReference, MutableList<Showing>> {
            for (event in events) {
                val movie = movies[event.movieId.lowercase()] ?: continue
                val list = getOrPut(MovieReferenceFromResponse(movie)) { mutableListOf() }
                list.add(ShowingFromResponse(event, cinema))
            }
        }
        result(Result.success(output))
    }

}

