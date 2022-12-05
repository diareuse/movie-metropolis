package movie.core.nwk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import movie.core.nwk.serializer.MinutesDurationSerializer
import movie.core.nwk.serializer.YearSerializer
import java.util.Date
import kotlin.time.Duration

@Serializable
data class MovieResponse(
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