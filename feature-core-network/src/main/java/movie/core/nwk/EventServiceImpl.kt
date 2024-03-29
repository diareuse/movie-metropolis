package movie.core.nwk

import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import movie.core.nwk.model.BodyResponse
import movie.core.nwk.model.ExtendedMovieResponse
import movie.core.nwk.model.MovieDetailsResponse
import movie.core.nwk.model.MovieEventResponse
import movie.core.nwk.model.NearbyCinemaResponse
import movie.core.nwk.model.ShowingType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal class EventServiceImpl(
    private val client: LazyHttpClient,
    private val clientQuickbook: LazyHttpClient
) : EventService {

    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)

    override suspend fun getEventsInCinema(
        cinema: String,
        date: Date
    ) = clientQuickbook.getOrCreate().runCatching {
        get {
            url("film-events/in-cinema/$cinema/at-date/${formatter.format(date)}")
        }.requireBody<BodyResponse<MovieEventResponse>>()
    }

    override suspend fun getNearbyCinemas(
        lat: Double,
        lng: Double
    ) = client.getOrCreate().runCatching {
        get {
            url("cinema/bylocation")
            parameter("lat", lat)
            parameter("long", lng)
            parameter("unit", "KILOMETERS")
        }.requireBody<BodyResponse<List<NearbyCinemaResponse>>>()
    }

    override suspend fun getDetail(id: String) = client.getOrCreate().runCatching {
        get {
            url("films/byDistributorCode/$id")
            parameter("lang", Locale.getDefault().language)
        }.requireBody<BodyResponse<MovieDetailsResponse>>()
    }

    override suspend fun getMoviesByType(type: ShowingType) =
        client.getOrCreate().runCatching {
            get {
                url("films/by-showing-type/${type.value}")
                parameter("ordering", "asc")
                parameter("lang", Locale.getDefault().language)
            }.requireBody<BodyResponse<List<ExtendedMovieResponse>>>()
        }

}