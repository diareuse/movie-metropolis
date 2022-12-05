package movie.core.nwk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import movie.core.nwk.serializer.LocalTimestampSerializer
import java.util.Date
import java.util.Locale

@Serializable
data class EventResponse(
    @SerialName("id") val id: String,
    @SerialName("filmId") val movieId: String,
    @SerialName("cinemaId") val cinemaId: String,
    @Serializable(LocalTimestampSerializer::class)
    @SerialName("eventDateTime") val startsAt: Date,
    @SerialName("bookingLink") val bookingUrl: String,
    @SerialName("attributeIds") val tags: List<String>,
    @SerialName("soldOut") val soldOut: Boolean,
    @SerialName("auditorium") val auditorium: String
) {

    val language: String
        get() = when {
            DubbedTag in tags -> labelDubbed
            SubbedTag in tags -> labelSubbed
            else -> "-"
        }

    val type
        get() = buildList {
            if (ScreeningType2D in tags) add("2D")
            if (ScreeningType3D in tags) add("3D")
            if (ScreeningTypeDolbyAtmos in tags) add("Dolby Atmos")
            if (ScreeningType4DX in tags) add("4DX")
            if (ScreeningTypeIMAX in tags) add("IMAX")
            if (ScreeningTypeVIP in tags) add("VIP")
        }.joinToString(separator = " | ")

    private val labelDubbed
        get() = tags.firstAround(Dubbing, "-").orEmpty()
            .let(::Locale).let {
                it.displayName.ifEmpty { it.language.uppercase() }
            }

    private val labelSubbed
        get() = buildString {
            val original = tags.firstAround(Original, "-").orEmpty()
                .let(::Locale)
            append(original.displayName.ifEmpty { original.language.uppercase() })
            append(" (")
            val subs = tags.firstAround(Subbed, "-").orEmpty()
                .let(::Locale)
            append(subs.displayName.ifEmpty { subs.language.uppercase() })
            append(")")
        }

    private fun List<String>.firstAround(start: String, end: String) =
        firstOrNull { it.startsWith(start) }
            ?.around(start, end)

    private fun String.around(start: String, end: String) = substringAfter(start)
        .substringBefore(end)

    companion object {
        const val SubbedTag = "subbed"
        const val DubbedTag = "dubbed"
        const val Subbed = "first-subbed-lang-"
        const val Dubbing = "dubbed-lang-"
        const val Original = "original-lang-"
        const val ScreeningType2D = "2d"
        const val ScreeningType3D = "3d"
        const val ScreeningTypeDolbyAtmos = "dolby-atmos"
        const val ScreeningType4DX = "4dx"
        const val ScreeningTypeIMAX = "imax"
        const val ScreeningTypeVIP = "vip"
    }

}