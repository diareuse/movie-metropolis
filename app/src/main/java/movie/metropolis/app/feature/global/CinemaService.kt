package movie.metropolis.app.feature.global

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal interface CinemaService {

    suspend fun getCinemas(): ResultsResponse<List<CinemaResponse>>

}

@Serializable
data class ResultsResponse<T>(
    @SerialName("results") val results: T
)

@Serializable
data class CinemaResponse(
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
    @SerialName("city") val city: String
)

class StringAsDoubleSerializer : KSerializer<Double> {

    override val descriptor = PrimitiveSerialDescriptor("stringAsDouble", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Double {
        return decoder.decodeString().toDoubleOrNull() ?: 0.0
    }

    override fun serialize(encoder: Encoder, value: Double) {
        encoder.encodeString(value.toString())
    }

}