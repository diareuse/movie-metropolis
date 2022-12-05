package movie.core.nwk.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import movie.core.nwk.model.MovieDetailResponse
import movie.core.nwk.model.ExtendedMovieResponse.Media as ExtendedMedia
import movie.core.nwk.model.MovieDetailResponse.Media as DetailMedia

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

internal class DetailMediaSerializer :
    JsonContentPolymorphicSerializer<DetailMedia>(DetailMedia::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out MovieDetailResponse.Media> {
        return when (element.jsonObject["type"]?.jsonPrimitive?.content) {
            "Image" -> DetailMedia.Image.serializer()
            "Video" -> DetailMedia.Video.serializer()
            else -> DetailMedia.Noop.serializer()
        }
    }
}