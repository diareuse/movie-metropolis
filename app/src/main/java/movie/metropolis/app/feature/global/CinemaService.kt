package movie.metropolis.app.feature.global

import movie.metropolis.app.feature.global.model.remote.CinemaResponse
import movie.metropolis.app.feature.global.model.remote.ResultsResponse

internal interface CinemaService {

    suspend fun getCinemas(): Result<ResultsResponse<List<CinemaResponse>>>

}