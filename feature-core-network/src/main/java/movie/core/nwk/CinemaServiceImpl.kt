package movie.core.nwk

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import movie.core.nwk.model.CinemaResponse
import movie.core.nwk.model.ResultsResponse
import movie.log.logCatching
import java.util.Locale

class CinemaServiceImpl(
    private val client: HttpClient
) : CinemaService {

    override suspend fun getCinemas() = client.logCatching("cinemas") {
        get {
            url("cinema")
            parameter("lang", Locale.getDefault().language)
        }.requireBody<ResultsResponse<List<CinemaResponse>>>()
    }

}