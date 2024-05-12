package movie.cinema.city.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.Calendar
import java.util.Date

internal class YearSerializer : KSerializer<Date> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("year", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Date {
        return Calendar.getInstance().apply {
            time = Date(0)
            set(Calendar.YEAR, decoder.decodeString().toInt())
        }.time
    }

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(
            Calendar.getInstance().apply { time = value }.get(Calendar.YEAR).toString()
        )
    }
}