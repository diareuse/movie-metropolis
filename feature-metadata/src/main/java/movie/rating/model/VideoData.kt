package movie.rating.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import movie.rating.DateTimestampSerializer
import java.util.Date

@Serializable
data class VideoData(
    @SerialName("site")
    val site: String,
    @SerialName("type")
    val type: String,
    @SerialName("key")
    val key: String,
    @SerialName("size")
    val size: Int,
    @SerialName("official")
    val official: Boolean,
    @Serializable(with = DateTimestampSerializer::class)
    @SerialName("published_at")
    val timestamp: Date
)