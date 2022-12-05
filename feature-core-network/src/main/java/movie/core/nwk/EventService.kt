package movie.core.nwk

import movie.core.nwk.model.BodyResponse
import movie.core.nwk.model.ExtendedMovieResponse
import movie.core.nwk.model.MovieDetailsResponse
import movie.core.nwk.model.MovieEventResponse
import movie.core.nwk.model.NearbyCinemaResponse
import movie.core.nwk.model.ShowingType
import java.util.Date

interface EventService {

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