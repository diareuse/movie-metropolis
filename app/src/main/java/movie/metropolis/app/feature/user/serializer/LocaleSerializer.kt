package movie.metropolis.app.feature.user.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.Locale

internal class LocaleSerializer : KSerializer<Locale> {

    override val descriptor = PrimitiveSerialDescriptor("locale", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Locale {
        return Locale(decoder.decodeString().replace("_", "-"))
    }

    override fun serialize(encoder: Encoder, value: Locale) {
        encoder.encodeString(value.toLanguageTag().replace("-", "_"))
    }

}