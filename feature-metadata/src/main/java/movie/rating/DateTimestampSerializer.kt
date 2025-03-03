package movie.rating

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateTimestampSerializer : KSerializer<Date> {
    private val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    override val descriptor = PrimitiveSerialDescriptor("date", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): Date {
        return parser.runCatching { parse(decoder.decodeString()) }.getOrNull() ?: Date(0)
    }

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(parser.format(value))
    }
}