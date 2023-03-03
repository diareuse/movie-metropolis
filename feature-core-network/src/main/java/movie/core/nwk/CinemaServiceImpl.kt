package movie.core.nwk

import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import movie.core.nwk.model.CinemaResponse
import movie.core.nwk.model.PromoCardResponse
import movie.core.nwk.model.ResultsResponse
import java.util.Locale

class CinemaServiceImpl(
    private val client: LazyHttpClient
) : CinemaService {

    override suspend fun getCinemas() = client.getOrCreate().runCatching {
        get {
            url("cinema")
            parameter("lang", Locale.getDefault().language)
        }.requireBody<ResultsResponse<List<CinemaResponse>>>()
    }

    override suspend fun getPromoCards() = client.getOrCreate().runCatching {
        get {
            url("film-promo-cards")
            parameter("lang", Locale.getDefault().language)
        }.requireBody<ResultsResponse<List<PromoCardResponse>>>()
    }

}