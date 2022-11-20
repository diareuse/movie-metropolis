package movie.metropolis.app.feature.global.model

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import movie.metropolis.app.feature.global.serializer.LocalTimestampSerializer
import movie.metropolis.app.feature.global.serializer.MinutesDurationSerializer
import movie.metropolis.app.feature.global.serializer.StringAsIntSerializer
import movie.metropolis.app.feature.global.serializer.YearSerializer
import java.util.Date
import kotlin.time.Duration

@Serializable
internal data class MovieDetailResponse(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("originalName") val nameOriginal: String,
    @Serializable(MinutesDurationSerializer::class)
    @SerialName("length") val duration: Duration,
    @SerialName("link") val url: String,
    @Serializable(YearSerializer::class)
    @SerialName("releaseYear") val releasedAt: Date,
    @SerialName("releaseCountry") val countryOfOrigin: String,
    @SerialName("cast") val cast: String,
    @SerialName("directors") val directors: String,
    @SerialName("synopsis") val synopsis: String,
    @Serializable(LocalTimestampSerializer::class)
    @SerialName("dateStarted") val screeningFrom: Date,
    @SerialName("categoriesAttributes") val genres: List<String>,
    @SerialName("categoryIds") val categories: List<Int>,
    @SerialName("screeningAttributes") val screeningTags: List<String>,
    @SerialName("ageRestrictionLink") val restrictionUrl: String,
    @SerialName("mediaList") val media: List<@Polymorphic Media>
) {

    sealed class Media {

        @Serializable
        @SerialName("Image")
        data class Image(
            @Serializable(StringAsIntSerializer::class)
            @SerialName("dimensionWidth") val width: Int, //serialize as string
            @Serializable(StringAsIntSerializer::class)
            @SerialName("dimensionHeight") val height: Int,
            @SerialName("") val url: String
        ) : Media()

        @Serializable
        @SerialName("Video")
        data class Video(
            @SerialName("subType") val type: String, //trailer is known
            @SerialName("url") val url: String
        ) : Media()

    }

}