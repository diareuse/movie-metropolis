package movie.core.nwk

import movie.core.nwk.model.CinemaResponse
import movie.core.nwk.model.ResultsResponse

interface CinemaService {

    suspend fun getCinemas(): Result<ResultsResponse<List<CinemaResponse>>>

}