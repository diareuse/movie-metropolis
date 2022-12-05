package movie.core.nwk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsResponse(
    @SerialName("filmDetails") val details: MovieDetailResponse
)