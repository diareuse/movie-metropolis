package movie.cinema.city.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal class StringAsIntSerializer : KSerializer<Int> {

    override val descriptor = PrimitiveSerialDescriptor("stringAsInt", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Int {
        return decoder.decodeString().toIntOrNull() ?: 0
    }

    override fun serialize(encoder: Encoder, value: Int) {
        encoder.encodeString(value.toString())
    }

}