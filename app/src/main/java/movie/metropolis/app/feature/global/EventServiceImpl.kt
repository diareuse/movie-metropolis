package movie.metropolis.app.feature.global

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import movie.metropolis.app.feature.global.model.remote.BodyResponse
import movie.metropolis.app.feature.global.model.remote.ExtendedMovieResponse
import movie.metropolis.app.feature.global.model.remote.MovieDetailsResponse
import movie.metropolis.app.feature.global.model.remote.MovieEventResponse
import movie.metropolis.app.feature.global.model.remote.NearbyCinemaResponse
import movie.metropolis.app.feature.global.model.remote.ShowingType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal class EventServiceImpl(
    private val client: HttpClient
) : EventService {

    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)

    override suspend fun getEventsInCinema(
        cinema: String,
        date: Date
    ) = kotlin.runCatching {
        client.get {
            url("quickbook/10101/film-events/in-cinema/$cinema/at-date/${formatter.format(date)}")
        }.body<BodyResponse<MovieEventResponse>>()
    }.onFailure { it.printStackTrace() }

    override suspend fun getNearbyCinemas(
        lat: Double,
        lng: Double
    ) = kotlin.runCatching {
        client.get {
            url("10101/cinema/bylocation")
            parameter("lat", lat)
            parameter("long", lng)
            parameter("unit", "KILOMETERS")
        }.body<BodyResponse<List<NearbyCinemaResponse>>>()
    }.onFailure { it.printStackTrace() }

    override suspend fun getDetail(id: String) = kotlin.runCatching {
        client.get {
            url("10101/films/byDistributorCode/$id")
            parameter("lang", Locale.getDefault().language)
        }.body<BodyResponse<MovieDetailsResponse>>()
    }.onFailure { it.printStackTrace() }

    override suspend fun getMoviesByType(type: ShowingType) = kotlin.runCatching {
        client.get {
            url("10101/films/by-showing-type/${type.value}")
            parameter("ordering", "asc")
            parameter("lang", Locale.getDefault().language)
        }.body<BodyResponse<List<ExtendedMovieResponse>>>()
    }.onFailure { it.printStackTrace() }

}