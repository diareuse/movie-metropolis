package movie.metropolis.app.feature.global.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MovieDetailsResponse(
    @SerialName("filmDetails") val details: MovieDetailResponse
)