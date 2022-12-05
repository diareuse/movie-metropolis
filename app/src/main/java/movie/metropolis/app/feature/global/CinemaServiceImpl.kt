package movie.metropolis.app.feature.global

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import movie.metropolis.app.feature.global.model.remote.CinemaResponse
import movie.metropolis.app.feature.global.model.remote.ResultsResponse
import movie.metropolis.app.util.requireBody
import java.util.Locale

internal class CinemaServiceImpl(
    private val client: HttpClient
) : CinemaService {

    override suspend fun getCinemas() = kotlin.runCatching {
        client.get {
            url("cinema")
            parameter("lang", Locale.getDefault().language)
        }.requireBody<ResultsResponse<List<CinemaResponse>>>()
    }

}