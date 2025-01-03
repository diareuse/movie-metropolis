package movie.cinema.city.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import movie.cinema.city.serializer.StringAsDoubleSerializer

@Serializable
internal data class CinemaResponse(
    @SerialName("postalCode") val postalCode: String,
    @SerialName("externalCode") val id: String,
    @SerialName("addressLine1") val addressLine: String,
    @SerialName("addressLine2") val addressLine2: String?,
    @Serializable(StringAsDoubleSerializer::class)
    @SerialName("latitude") val latitude: Double,
    @Serializable(StringAsDoubleSerializer::class)
    @SerialName("longitude") val longitude: Double,
    @SerialName("description") val description: String,
    @SerialName("displayName") val name: String,
    @SerialName("city") val city: String,
    @SerialName("cinemaImage") val image: String
)