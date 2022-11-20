package movie.metropolis.app.feature.global

import java.util.Date

typealias MovieWithShowings = Map<MovieReference, Iterable<Showing>>

interface EventFeature {

    suspend fun getShowings(cinema: Cinema, at: Date): Result<MovieWithShowings>
    suspend fun getCinemas(location: Location?): Result<Iterable<Cinema>>
    suspend fun getMovie(distributorCode: String): Result<Movie>
    suspend fun getUpcoming(): Result<Iterable<MovieUpcoming>>

}