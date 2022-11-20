package movie.metropolis.app.feature.global

import movie.metropolis.app.feature.global.model.BodyResponse
import movie.metropolis.app.feature.global.model.ExtendedMovieResponse
import movie.metropolis.app.feature.global.model.MovieDetailsResponse
import movie.metropolis.app.feature.global.model.MovieEventResponse
import movie.metropolis.app.feature.global.model.NearbyCinemaResponse
import movie.metropolis.app.feature.global.model.ShowingType
import java.util.Date

internal interface EventService {

    suspend fun getEventsInCinema(cinema: String, date: Date): BodyResponse<MovieEventResponse>
    suspend fun getNearbyCinemas(lat: Double, lng: Double): BodyResponse<NearbyCinemaResponse>
    suspend fun getDetail(distributorCode: String): BodyResponse<MovieDetailsResponse>
    suspend fun getMoviesByType(type: ShowingType): BodyResponse<ExtendedMovieResponse>

}