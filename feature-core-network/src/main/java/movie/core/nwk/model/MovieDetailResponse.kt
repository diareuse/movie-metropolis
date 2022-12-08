package movie.core.nwk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import movie.core.nwk.serializer.DetailMediaSerializer
import movie.core.nwk.serializer.LocalTimestampSerializer
import movie.core.nwk.serializer.MinutesDurationSerializer
import movie.core.nwk.serializer.StringAsIntSerializer
import movie.core.nwk.serializer.YearSerializer
import java.util.Date
import kotlin.time.Duration

@Serializable
data class MovieDetailResponse(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("originalName") val nameOriginal: String,
    @Serializable(MinutesDurationSerializer::class)
    @SerialName("length") val duration: Duration,
    @SerialName("link") val url: String,
    @Serializable(YearSerializer::class)
    @SerialName("releaseYear") val releasedAt: Date,
    @SerialName("releaseCountry") val countryOfOrigin: String?,
    @SerialName("cast") val cast: String?,
    @SerialName("directors") val directors: String,
    @SerialName("synopsis") val synopsis: String?,
    @Serializable(LocalTimestampSerializer::class)
    @SerialName("dateStarted") val screeningFrom: Date,
    @SerialName("categoriesAttributes") val genres: List<String>,
    @SerialName("categoryIds") val categories: List<Int>,
    @SerialName("screeningAttributes") val screeningTags: List<String>,
    @SerialName("ageRestrictionLink") val restrictionUrl: String,
    @SerialName("mediaList") val media: List<@Serializable(DetailMediaSerializer::class) Media>
) {

    sealed class Media {

        @Serializable
        data class Image(
            @Serializable(StringAsIntSerializer::class)
            @SerialName("dimensionWidth") val width: Int, //serialize as string
            @Serializable(StringAsIntSerializer::class)
            @SerialName("dimensionHeight") val height: Int,
            @SerialName("url") val url: String
        ) : Media()

        @Serializable
        data class Video(
            @SerialName("subType") val type: String, //trailer is known
            @SerialName("url") val url: String
        ) : Media()

        @Serializable
        object Noop : Media()

    }

}