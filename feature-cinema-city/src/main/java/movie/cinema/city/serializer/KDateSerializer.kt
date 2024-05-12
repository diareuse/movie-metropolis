package movie.cinema.city.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal abstract class KDateSerializer : KSerializer<Date> {

    final override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor(type, PrimitiveKind.STRING)

    abstract val type: String
    abstract val formatter: SimpleDateFormat

    private val fallback by lazy {
        android.icu.text.SimpleDateFormat(
            formatter.toPattern(),
            Locale.getDefault()
        )
    }

    final override fun deserialize(decoder: Decoder): Date {
        val str = decoder.decodeString()
        return try {
            formatter.parse(str).let(::requireNotNull)
        } catch (e: Throwable) {
            try {
                fallback.parse(str).let(::requireNotNull)
            } catch (e: Throwable) {
                throw RuntimeException(
                    "Parse error: value='$str', format='${formatter.toPattern()}'",
                    e
                )
            }
        }
    }

    final override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(formatter.format(value))
    }

}