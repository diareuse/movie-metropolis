package movie.core.nwk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import movie.core.nwk.serializer.ExtendedMediaSerializer
import movie.core.nwk.serializer.LocalTimestampSerializer
import movie.core.nwk.serializer.LocaleSerializer
import movie.core.nwk.serializer.MinutesDurationSerializer
import movie.core.nwk.serializer.StringAsIntSerializer
import movie.core.nwk.serializer.YearSerializer
import java.util.Date
import java.util.Locale
import kotlin.time.Duration

@Serializable
data class ExtendedMovieResponse(
    @SerialName("key") val id: Key,
    @SerialName("link") val url: String,
    @Serializable(YearSerializer::class)
    @SerialName("releaseYear") val releasedAt: Date,
    @Serializable(LocalTimestampSerializer::class)
    @SerialName("dateStarted") val screeningFrom: Date,
    @Serializable(MinutesDurationSerializer::class)
    @SerialName("length") val duration: Duration,
    @SerialName("exportCodes") val distributorCodes: List<String>,
    @SerialName("media") val media: List<@Serializable(ExtendedMediaSerializer::class) Media>,
    @SerialName("i18nFieldsMap") val metadata: Map<@Serializable(LocaleSerializer::class) Locale, Metadata>
) {

    @Serializable
    data class Key(
        @SerialName("key") val key: String
    )

    sealed class Media {

        @Serializable
        data class Image(
            @Serializable(StringAsIntSerializer::class)
            @SerialName("dimensionWidth") val width: Int,
            @Serializable(StringAsIntSerializer::class)
            @SerialName("dimensionHeight") val height: Int,
            @SerialName("url") val url: String
        ) : Media()

        @Serializable
        data class Video(
            @SerialName("url") val url: String,
            @SerialName("subType") val type: String
        ) : Media()

        @Serializable
        object Noop : Media()

    }

    @Serializable
    data class Metadata(
        @SerialName("name") val name: String,
        @SerialName("synopsis") val synopsis: String?,
        @SerialName("directors") val directors: String?,
        @SerialName("cast") val cast: String?,
        @SerialName("production") val countryOfOrigin: String?
    )

}