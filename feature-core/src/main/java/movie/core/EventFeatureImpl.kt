package movie.core

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import movie.core.adapter.CinemaFromResponse
import movie.core.adapter.MovieDetailFromResponse
import movie.core.adapter.MovieFromResponse
import movie.core.adapter.MoviePreviewFromResponse
import movie.core.adapter.ShowingFromResponse
import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.model.Movie
import movie.core.model.MovieDetail
import movie.core.model.MoviePreview
import movie.core.model.Showing
import java.util.Date
import kotlin.math.abs

internal class EventFeatureImpl(
    private val event: movie.core.nwk.EventService,
    private val cinema: movie.core.nwk.CinemaService
) : EventFeature {

    override suspend fun getShowings(cinema: Cinema, at: Date): Result<MovieWithShowings> {
        return event.getEventsInCinema(cinema.id, at).map { response ->
            val events = response.body.events
            val movies = response.body.movies
            events
                .groupBy { it.movieId }
                .map { (movieId, events) ->
                    val movie = movies.first { it.id == movieId }
                    val reference = MovieFromResponse(movie)
                    val showings = events.map { event -> ShowingFromResponse(event, cinema) }
                    reference to showings
                }
                .toMap()
        }
    }

    override suspend fun getShowings(
        movie: Movie,
        at: Date,
        location: Location
    ): Result<CinemaWithShowings> = coroutineScope {
        val cinemas = getCinemas(location).getOrDefault(emptyList())
        val showings = mutableListOf<Deferred<Pair<Cinema, Iterable<Showing>>>>()
        for (cinema in cinemas) {
            showings += async {
                cinema to getShowings(cinema, at)
                    .map { it[it.keys.find { it.id == movie.id }] ?: emptyList() }
                    .getOrDefault(emptyList())
            }
        }
        Result.success(showings.awaitAll().toMap())
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
        return event.getMoviesByType(movie.core.nwk.model.ShowingType.Current)
            .map { it.body.map(::MoviePreviewFromResponse) }
            .map { it.sortedBy { abs(Date().time - it.screeningFrom.time) } }
    }

    override suspend fun getUpcoming(): Result<Iterable<MoviePreview>> {
        return event.getMoviesByType(movie.core.nwk.model.ShowingType.Upcoming)
            .map { it.body.map(::MoviePreviewFromResponse) }
            .map { it.sortedBy { abs(Date().time - it.screeningFrom.time) } }
    }
}