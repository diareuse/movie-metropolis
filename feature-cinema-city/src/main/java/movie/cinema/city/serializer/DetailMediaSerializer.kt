package movie.cinema.city.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import movie.cinema.city.model.MovieDetailResponse

internal class DetailMediaSerializer :
    JsonContentPolymorphicSerializer<MovieDetailResponse.Media>(MovieDetailResponse.Media::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out MovieDetailResponse.Media> {
        return when (element.jsonObject["type"]?.jsonPrimitive?.content) {
            "Image" -> MovieDetailResponse.Media.Image.serializer()
            "Video" -> MovieDetailResponse.Media.Video.serializer()
            else -> MovieDetailResponse.Media.Noop.serializer()
        }
    }
}