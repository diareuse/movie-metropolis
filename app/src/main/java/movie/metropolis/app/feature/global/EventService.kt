package movie.metropolis.app.feature.global

import movie.metropolis.app.feature.global.model.BodyResponse
import movie.metropolis.app.feature.global.model.ExtendedMovieResponse
import movie.metropolis.app.feature.global.model.MovieDetailsResponse
import movie.metropolis.app.feature.global.model.MovieEventResponse
import movie.metropolis.app.feature.global.model.NearbyCinemaResponse
import movie.metropolis.app.feature.global.model.ShowingType
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