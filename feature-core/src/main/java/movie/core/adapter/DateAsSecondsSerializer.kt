package movie.core.adapter

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.Date
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class DateAsSecondsSerializer : KSerializer<Date> {

    override val descriptor = PrimitiveSerialDescriptor("date", PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): Date {
        return Date(decoder.decodeLong().seconds.inWholeMilliseconds)
    }

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeLong(value.time.milliseconds.inWholeSeconds)
    }

}