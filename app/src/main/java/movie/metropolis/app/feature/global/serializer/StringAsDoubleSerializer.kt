package movie.metropolis.app.feature.global.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal class StringAsDoubleSerializer : KSerializer<Double> {

    override val descriptor = PrimitiveSerialDescriptor("stringAsDouble", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Double {
        return decoder.decodeString().toDoubleOrNull() ?: 0.0
    }

    override fun serialize(encoder: Encoder, value: Double) {
        encoder.encodeString(value.toString())
    }

}