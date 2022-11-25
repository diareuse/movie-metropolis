package movie.metropolis.app.feature.global

import java.util.Date

typealias MovieWithShowings = Map<MovieReference, Iterable<Showing>>
typealias CinemaWithShowings = Map<Cinema, Iterable<Showing>>

interface EventFeature {

    suspend fun getShowings(cinema: Cinema, at: Date): Result<MovieWithShowings>
    suspend fun getShowings(movie: Movie, at: Date, location: Location): Result<CinemaWithShowings>
    suspend fun getCinemas(location: Location?): Result<Iterable<Cinema>>
    suspend fun getDetail(movie: Movie): Result<MovieDetail>
    suspend fun getCurrent(): Result<Iterable<MoviePreview>>
    suspend fun getUpcoming(): Result<Iterable<MoviePreview>>

}