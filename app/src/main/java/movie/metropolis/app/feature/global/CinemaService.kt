package movie.metropolis.app.feature.global

import movie.metropolis.app.feature.global.model.CinemaResponse
import movie.metropolis.app.feature.global.model.ResultsResponse

internal interface CinemaService {

    suspend fun getCinemas(): Result<ResultsResponse<List<CinemaResponse>>>

}