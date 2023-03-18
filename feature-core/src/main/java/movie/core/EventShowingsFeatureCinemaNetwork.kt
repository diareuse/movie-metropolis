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

    override suspend fun get(date: Date) = service.getEventsInCinema(cinema.id, date)
        .map { response ->
            val events = response.body.events
            val movies = response.body.movies.associateBy { it.id.lowercase() }
            buildMap<MovieReference, MutableList<Showing>> {
                for (event in events) {
                    val movie = movies[event.movieId.lowercase()] ?: continue
                    val list = getOrPut(MovieReferenceFromResponse(movie)) { mutableListOf() }
                    list.add(ShowingFromResponse(event, cinema))
                }
            }
        }

}