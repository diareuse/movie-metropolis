package movie.cinema.city.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

internal class MinutesDurationSerializer : KSerializer<Duration> {

    override val descriptor = PrimitiveSerialDescriptor("minutes", PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): Duration {
        return decoder.decodeLong().minutes
    }

    override fun serialize(encoder: Encoder, value: Duration) {
        encoder.encodeLong(value.inWholeMinutes)
    }

}