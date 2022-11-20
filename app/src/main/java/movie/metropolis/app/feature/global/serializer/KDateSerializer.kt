package movie.metropolis.app.feature.global.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.Date

internal abstract class KDateSerializer : KSerializer<Date> {

    final override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor(type, PrimitiveKind.STRING)

    abstract val type: String
    abstract val formatter: SimpleDateFormat

    final override fun deserialize(decoder: Decoder): Date {
        return formatter.parse(decoder.decodeString()).let(::requireNotNull)
    }

    final override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(formatter.format(value))
    }

}