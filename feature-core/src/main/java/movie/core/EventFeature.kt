package movie.core

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.model.Movie
import movie.core.model.MovieDetail
import movie.core.model.MoviePreview
import movie.core.model.MovieReference
import movie.core.model.Showing
import java.util.Date

typealias MovieWithShowings = Map<MovieReference, Iterable<Showing>>
typealias CinemaWithShowings = Map<Cinema, Iterable<Showing>>

interface EventFeature {

    suspend fun getShowings(cinema: Cinema, at: Date): Result<MovieWithShowings>
    suspend fun getShowings(
        movie: Movie,
        at: Date,
        location: Location
    ): Result<CinemaWithShowings> = coroutineScope {
        val cinemas = getCinemas(location).getOrDefault(emptyList())
        val showings = mutableListOf<Deferred<Pair<Cinema, Iterable<Showing>>>>()
        for (cinema in cinemas) {
            showings += async {
                cinema to getShowings(cinema, at)
                    .mapCatching {
                        it.getValue(it.keys.first {
                            it.id.equals(
                                movie.id,
                                ignoreCase = true
                            )
                        })
                    }
                    .getOrDefault(emptyList())
            }
        }
        Result.success(showings.awaitAll().toMap())
    }

    suspend fun getCinemas(location: Location?): Result<Iterable<Cinema>>
    suspend fun getDetail(movie: Movie): Result<MovieDetail>
    suspend fun getCurrent(): Result<Iterable<MoviePreview>>
    suspend fun getUpcoming(): Result<Iterable<MoviePreview>>

}