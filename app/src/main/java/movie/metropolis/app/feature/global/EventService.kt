package movie.metropolis.app.feature.global

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import movie.metropolis.app.feature.user.LocaleSerializer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

internal interface EventService {

    suspend fun getEventsInCinema(cinema: String, date: Date): BodyResponse<MovieEventResponse>
    suspend fun getNearbyCinemas(lat: Double, lng: Double): BodyResponse<NearbyCinemaResponse>
    suspend fun getDetail(distributorCode: String): BodyResponse<MovieDetailsResponse>
    suspend fun getMoviesByType(type: ShowingType): BodyResponse<ExtendedMovieResponse>

}

@Serializable
internal data class ExtendedMovieResponse(
    @SerialName("key") val id: Key,
    @SerialName("link") val url: String,
    @Serializable(YearSerializer::class)
    @SerialName("releaseYear") val releasedAt: Date,
    @Serializable(LocalTimestampSerializer::class)
    @SerialName("dateStarted") val screeningFrom: Date,
    @Serializable(MinutesDurationSerializer::class)
    @SerialName("length") val duration: Duration,
    @SerialName("exportCodes") val distributorCodes: List<String>,
    @SerialName("media") val media: List<@Polymorphic Media>,
    @SerialName("i18nFieldsMap") val metadata: Map<@Serializable(LocaleSerializer::class) Locale, Metadata>
) {

    @Serializable
    data class Key(
        @SerialName("key") val key: String
    )

    sealed class Media {

        @Serializable
        @SerialName("Image")
        data class Image(
            @Serializable(StringAsIntSerializer::class)
            @SerialName("dimensionWidth") val width: Int,
            @Serializable(StringAsIntSerializer::class)
            @SerialName("dimensionHeight") val height: Int,
            @SerialName("url") val url: String
        ) : Media()

        @Serializable
        @SerialName("Video")
        data class Video(
            @SerialName("url") val url: String,
            @SerialName("subType") val type: String
        ) : Media()

    }

    @Serializable
    data class Metadata(
        @SerialName("name") val name: String,
        @SerialName("synopsis") val synopsis: String,
        @SerialName("directors") val directors: String,
        @SerialName("cast") val cast: String?,
        @SerialName("production") val countryOfOrigin: String
    )

}

internal enum class ShowingType(val value: String) {

    Current("SHOWING"), Upcoming("FUTURE")

}

@Serializable
internal data class MovieDetailsResponse(
    @SerialName("filmDetails") val details: MovieDetailResponse
)

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

internal class StringAsIntSerializer : KSerializer<Int> {

    override val descriptor = PrimitiveSerialDescriptor("stringAsInt", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Int {
        return decoder.decodeString().toIntOrNull() ?: 0
    }

    override fun serialize(encoder: Encoder, value: Int) {
        encoder.encodeString(value.toString())
    }

}


@Serializable
internal data class NearbyCinemaResponse(
    @SerialName("theatreCode") val cinemaId: String,
    @SerialName("straightLineDistance") val distanceKms: Double
)

@Serializable
internal data class BodyResponse<T>(
    @SerialName("body") val body: T
)

@Serializable
internal data class MovieEventResponse(
    @SerialName("films") val movies: List<MovieResponse>,
    @SerialName("events") val events: List<EventResponse>
)

@Serializable
internal data class MovieResponse(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @Serializable(MinutesDurationSerializer::class)
    @SerialName("length") val duration: Duration,
    @SerialName("posterLink") val posterUrl: String,
    @SerialName("videoLink") val videoUrl: String,
    @SerialName("link") val url: String,
    @Serializable(YearSerializer::class)
    @SerialName("releaseYear") val releasedAt: Date,
    @SerialName("attributeIds") val tags: List<String>
)

@Serializable
internal data class EventResponse(
    @SerialName("id") val id: String,
    @SerialName("filmId") val movieId: String,
    @SerialName("cinemaId") val cinemaId: String,
    @Serializable(LocalTimestampSerializer::class)
    @SerialName("eventDateTime") val startsAt: Date,
    @SerialName("bookingLink") val bookingUrl: String,
    @SerialName("attributeIds") val tags: List<String>,
    @SerialName("soldOut") val soldOut: Boolean,
    @SerialName("auditorium") val auditorium: String
)

internal class MinutesDurationSerializer : KSerializer<Duration> {

    override val descriptor = PrimitiveSerialDescriptor("minutes", PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): Duration {
        return decoder.decodeLong().minutes
    }

    override fun serialize(encoder: Encoder, value: Duration) {
        encoder.encodeLong(value.inWholeMinutes)
    }

}

internal abstract class KDateSerializer : KSerializer<Date> {

    final override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor(type, PrimitiveKind.STRING)

    abstract val type: String
    abstract val formatter: SimpleDateFormat

    final override fun deserialize(decoder: Decoder): Date {
        return formatter.parse(decoder.decodeString()).let(::requireNotNull)
    }

    final override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(formatter.format(value))
    }

}

internal class YearSerializer : KDateSerializer() {

    override val type: String = "year"
    override val formatter = SimpleDateFormat("yyyy", Locale.ROOT).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

}

internal class LocalTimestampSerializer : KDateSerializer() {

    override val type = "timestamp"
    override val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT).apply {
        timeZone = TimeZone.getTimeZone("Europe/Prague")
    }

}