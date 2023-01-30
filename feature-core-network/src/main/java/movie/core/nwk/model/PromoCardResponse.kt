package movie.core.nwk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PromoCardResponse(
    @SerialName("filmExportCode") val id: String,
    @SerialName("filmPromoImage") val imageId: String
)