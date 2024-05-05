package movie.cinema.city.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import movie.cinema.city.model.ExtendedMovieResponse.Media as ExtendedMedia

internal class ExtendedMediaSerializer :
    JsonContentPolymorphicSerializer<ExtendedMedia>(ExtendedMedia::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out ExtendedMedia> {
        return when (element.jsonObject["type"]?.jsonPrimitive?.content) {
            "Image" -> ExtendedMedia.Image.serializer()
            "Video" -> ExtendedMedia.Video.serializer()
            else -> ExtendedMedia.Noop.serializer()
        }
    }
}