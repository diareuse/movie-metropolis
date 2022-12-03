package movie.metropolis.app.feature.global

import movie.metropolis.app.feature.global.model.remote.BodyResponse
import movie.metropolis.app.feature.global.model.remote.ExtendedMovieResponse
import movie.metropolis.app.feature.global.model.remote.MovieDetailsResponse
import movie.metropolis.app.feature.global.model.remote.MovieEventResponse
import movie.metropolis.app.feature.global.model.remote.NearbyCinemaResponse
import movie.metropolis.app.feature.global.model.remote.ShowingType
import java.util.Date

internal interface EventService {

    suspend fun getEventsInCinema(
        cinema: String,
        date: Date
    ): Result<BodyResponse<MovieEventResponse>>

    suspend fun getNearbyCinemas(
        lat: Double,
        lng: Double
    ): Result<BodyResponse<List<NearbyCinemaResponse>>>

    suspend fun getDetail(id: String): Result<BodyResponse<MovieDetailsResponse>>
    suspend fun getMoviesByType(type: ShowingType): Result<BodyResponse<List<ExtendedMovieResponse>>>

}