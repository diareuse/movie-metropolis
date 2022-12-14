package movie.core

import movie.core.adapter.CinemaFromResponse
import movie.core.adapter.MovieDetailFromResponse
import movie.core.adapter.MoviePreviewFromResponse
import movie.core.adapter.MovieReferenceFromResponse
import movie.core.adapter.ShowingFromResponse
import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.model.Movie
import movie.core.model.MovieDetail
import movie.core.model.MoviePreview
import movie.core.nwk.CinemaService
import movie.core.nwk.EventService
import movie.core.nwk.model.ShowingType
import java.util.Date

internal class EventFeatureImpl(
    private val event: EventService,
    private val cinema: CinemaService
) : EventFeature {

    override suspend fun getShowings(cinema: Cinema, at: Date): Result<MovieWithShowings> {
        return event.getEventsInCinema(cinema.id, at).map { response ->
            val events = response.body.events
            val movies = response.body.movies
            events
                .groupBy { it.movieId }
                .map { (movieId, events) ->
                    val movie = movies.first { it.id.equals(movieId, ignoreCase = true) }
                    val reference = MovieReferenceFromResponse(movie)
                    val showings = events.map { event -> ShowingFromResponse(event, cinema) }
                    reference to showings
                }
                .toMap()
        }
    }

    override suspend fun getCinemas(location: Location?): Result<Iterable<Cinema>> {
        val cinemas = cinema.getCinemas().map { it.results.map(::CinemaFromResponse) }
        return when (location) {
            null -> cinemas
            else -> {
                val nearby = event.getNearbyCinemas(location.latitude, location.longitude)
                    .map { it.body }
                    .getOrDefault(emptyList())
                if (cinemas.isFailure || nearby.isEmpty()) {
                    cinemas
                } else {
                    val cinemas = cinemas.getOrThrow()
                    val mapped = nearby.mapNotNull { nearby ->
                        cinemas.firstOrNull { it.id == nearby.cinemaId }
                            ?.copy(distance = nearby.distanceKms)
                    }
                    Result.success(mapped)
                }
            }
        }.map { it.sortedWith(compareBy(Cinema::distance, Cinema::city)) }
    }

    override suspend fun getDetail(movie: Movie): Result<MovieDetail> {
        return event.getDetail(movie.id).map { it.body.details }.map(::MovieDetailFromResponse)
    }

    override suspend fun getCurrent(): Result<Iterable<MoviePreview>> {
        return event.getMoviesByType(ShowingType.Current)
            .map { it.body.map(::MoviePreviewFromResponse) }
    }

    override suspend fun getUpcoming(): Result<Iterable<MoviePreview>> {
        return event.getMoviesByType(ShowingType.Upcoming)
            .map { it.body.map(::MoviePreviewFromResponse) }
    }
}