package movie.metropolis.app.feature.global.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import movie.metropolis.app.feature.global.model.ExtendedMovieResponse.Media

internal class MediaSerializer : JsonContentPolymorphicSerializer<Media>(Media::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out Media> {
        return when (element.jsonObject["type"]?.jsonPrimitive?.content) {
            "Image" -> Media.Image.serializer()
            "Video" -> Media.Video.serializer()
            else -> Media.Noop.serializer()
        }
    }
}