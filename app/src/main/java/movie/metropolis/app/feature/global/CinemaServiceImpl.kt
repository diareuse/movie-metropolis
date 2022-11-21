package movie.metropolis.app.feature.global

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import movie.metropolis.app.feature.global.model.CinemaResponse
import movie.metropolis.app.feature.global.model.ResultsResponse
import java.util.Locale

internal class CinemaServiceImpl(
    private val client: HttpClient
) : CinemaService {

    override suspend fun getCinemas() = kotlin.runCatching {
        client.get {
            url("/cinema")
            parameter("lang", Locale.getDefault().language)
        }.body<ResultsResponse<List<CinemaResponse>>>()
    }

}