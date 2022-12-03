package movie.metropolis.app.feature.global.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import movie.metropolis.app.feature.global.serializer.MinutesDurationSerializer
import movie.metropolis.app.feature.global.serializer.YearSerializer
import java.util.Date
import kotlin.time.Duration

@Serializable
internal data class MovieResponse(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @Serializable(MinutesDurationSerializer::class)
    @SerialName("length") val duration: Duration,
    @SerialName("posterLink") val posterUrl: String,
    @SerialName("videoLink") val videoUrl: String?,
    @SerialName("link") val url: String,
    @Serializable(YearSerializer::class)
    @SerialName("releaseYear") val releasedAt: Date,
    @SerialName("attributeIds") val tags: List<String>
)