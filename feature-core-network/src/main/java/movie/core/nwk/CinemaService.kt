package movie.core.nwk

import movie.core.nwk.model.CinemaResponse
import movie.core.nwk.model.PromoCardResponse
import movie.core.nwk.model.ResultsResponse

interface CinemaService {

    suspend fun getCinemas(): Result<ResultsResponse<List<CinemaResponse>>>
    suspend fun getPromoCards(): Result<ResultsResponse<List<PromoCardResponse>>>

}